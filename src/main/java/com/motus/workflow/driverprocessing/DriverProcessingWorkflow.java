package com.motus.workflow.driverprocessing;

import com.uber.cadence.workflow.QueryMethod;
import com.uber.cadence.workflow.WorkflowMethod;

public interface DriverProcessingWorkflow {

    @WorkflowMethod
    Results processDrivers(Arguments arguments);

    @QueryMethod(name="status")
    String getStatus();
}
