package com.motus.workflow;

import com.motus.workflow.driverprocessing.DriverProcessingWorkflowImpl;
import com.uber.cadence.worker.Worker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);

        Worker.Factory factory = new Worker.Factory("test-domain");
        Worker worker = factory.newWorker("DriverProcessingTaskList");
        worker.registerWorkflowImplementationTypes(DriverProcessingWorkflowImpl.class);
        factory.start();
    }

}
