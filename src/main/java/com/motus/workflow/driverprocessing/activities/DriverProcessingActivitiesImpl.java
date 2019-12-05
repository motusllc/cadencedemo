package com.motus.workflow.driverprocessing.activities;


import com.uber.cadence.activity.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverProcessingActivitiesImpl implements DriverProcessingActivities {

    private Logger logger = LoggerFactory.getLogger(DriverProcessingActivitiesImpl.class);

    @Override
    public DriverInfo getDriverInfo(String username) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Getting driver info for " + username + ", attempt # " + Activity.getTask().getAttempt());
        DriverInfo di = new DriverInfo();
        di.setUsername(username);
        di.setName(username + " Smith");
        return di;
    }

    @Override
    public DriverInfo getBankAccountNumber(DriverInfo info) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        info.setBankAccount(info.getUsername() + "-12345");
        logger.info("Getting bank account info for " + info.getUsername() + ", attempt # " + Activity.getTask().getAttempt());
        if (info.getUsername().equals("56623") && Activity.getTask().getAttempt() < 2) {
            throw new RuntimeException("foo");
        }
        return info;
    }

    @Override
    public void sendNachaFile(String fileContents) {
        System.out.println(String.format("Sending NACHA file: %n%s", fileContents));
    }

    @Override
    public void updatePaymentStatus(String username) {

    }
}
