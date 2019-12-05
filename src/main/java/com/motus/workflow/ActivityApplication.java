package com.motus.workflow;

import com.motus.workflow.driverprocessing.activities.DriverProcessingActivitiesImpl;
import com.uber.cadence.worker.Worker;
import com.uber.cadence.worker.WorkerOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActivityApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);

        Worker.Factory factory = new Worker.Factory("test-domain");
        WorkerOptions options = new WorkerOptions.Builder()
                .setMaxConcurrentActivityExecutionSize(2)
                .build();
        Worker worker = factory.newWorker("DriverProcessingTaskList", options);
        worker.registerActivitiesImplementations(new DriverProcessingActivitiesImpl());
        factory.start();
    }

}
