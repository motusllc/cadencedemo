package com.motus.workflow;

import com.motus.workflow.driverprocessing.Arguments;
import com.motus.workflow.driverprocessing.DriverProcessingWorkflow;
import com.motus.workflow.driverprocessing.Results;
import com.uber.cadence.WorkflowExecution;
import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TestWorkflowRunner {

    public static void main(String[] args) throws Exception {

        List<String> drivers = new ArrayList<>();
        drivers.add("12345");
        drivers.add("23456");
        drivers.add("56623");
        drivers.add("32256");
        drivers.add("asdfas");
        drivers.add("23asdfa456");
        drivers.add("dsf");
        drivers.add("322sdfsf56");

        Arguments arguments = new Arguments();
        arguments.setUsernames(drivers);

        WorkflowOptions options = new WorkflowOptions.Builder()
                .setExecutionStartToCloseTimeout(Duration.ofSeconds(200))
                .setTaskList("DriverProcessingTaskList")
                .build();

        WorkflowClient workflowClient = WorkflowClient.newInstance("test-domain");
        DriverProcessingWorkflow workflow = workflowClient.newWorkflowStub(DriverProcessingWorkflow.class, options);

        WorkflowExecution execution = WorkflowClient.start(workflow::processDrivers, arguments);

        System.out.println("Started process file workflow with workflowId=\"" + execution.getWorkflowId()
                + "\" and runId=\"" + execution.getRunId() + "\"");

        for (int i = 0; i < 10;  i++) {
            System.out.println("Status: " + workflow.getStatus());
            Thread.sleep(1000);
        }

       DriverProcessingWorkflow workflow2 = workflowClient.newWorkflowStub(DriverProcessingWorkflow.class, execution.getWorkflowId());

        // Returns result potentially waiting for workflow to complete.
        Results result = workflow2.processDrivers(arguments);

        System.out.println(result);
    }
}
