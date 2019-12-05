package com.motus.workflow.driverprocessing;

import com.motus.workflow.driverprocessing.activities.DriverInfo;
import com.motus.workflow.driverprocessing.activities.DriverProcessingActivities;
import com.uber.cadence.activity.ActivityOptions;
import com.uber.cadence.common.RetryOptions;
import com.uber.cadence.workflow.Async;
import com.uber.cadence.workflow.Promise;
import com.uber.cadence.workflow.Workflow;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DriverProcessingWorkflowImpl implements DriverProcessingWorkflow {

    private final Logger logger = Workflow.getLogger(DriverProcessingWorkflowImpl.class);

    private final DriverProcessingActivities activities;

    private String status;

    public DriverProcessingWorkflowImpl() {
        activities = Workflow.newActivityStub(DriverProcessingActivities.class,
                new ActivityOptions.Builder()
                        .setScheduleToCloseTimeout(Duration.ofSeconds(60))
                        .setRetryOptions(
                                new RetryOptions.Builder()
                                        .setInitialInterval(Duration.ofSeconds(1))
                                        .setMaximumAttempts(5)
                                        .setDoNotRetry(IllegalArgumentException.class)
                                        .build())
                        .build());
    }

    @Override
    public Results processDrivers(Arguments arguments) {

        Results results = new Results();

        this.status = "Starting";
        logger.info("Starting driver processing");

        // Get Driver info
        List<Promise<DriverInfo>> infoPromises = new ArrayList<>();
        for (String driver : arguments.getUsernames()) {
            Promise<DriverInfo> namePromise = Async.function(activities::getDriverInfo, driver);
            infoPromises.add(namePromise);
        }
        Promise<List<DriverInfo>> driverInfoPromises = Promise.allOf(infoPromises);
        this.status = "Getting Driver Info";
        List<DriverInfo> driverInfoList = driverInfoPromises.get();

        logger.info("Got all the driver info, getting account numbers");

        // Get Bank Account
        List<Promise<DriverInfo>> accountPromises = new ArrayList<>();
        for (DriverInfo driverInfo : driverInfoList) {
            Promise<DriverInfo> accountPromise = Async.function(activities::getBankAccountNumber, driverInfo);
            accountPromises.add(accountPromise);
        }
        Promise<List<DriverInfo>> bankAccountPromises = Promise.allOf(accountPromises);
        this.status = "Getting bank account info";
        driverInfoList = bankAccountPromises.get();

        logger.info("Got all the account numbers, creating nacha file");

        // Generate the NACHA file
        StringBuilder nachaFile = new StringBuilder();
        for (DriverInfo driverInfo : driverInfoList) {
            nachaFile.append(driverInfo.getUsername())
                    .append(":")
                    .append(driverInfo.getBankAccount());
        }

        logger.info("Done creating nacha file, now sending");
        this.status = "Sending Nacha File";
        activities.sendNachaFile(nachaFile.toString());

        logger.info("Updating payment status");
        for (String driver : arguments.getUsernames()) {
            activities.updatePaymentStatus(driver);
            results.getResults().put(driver, "SUCCESS");
        }

        this.status = "Done";
        logger.info("Done processing");

        return results;
    }

    @Override
    public String getStatus() {
        return status;
    }
}
