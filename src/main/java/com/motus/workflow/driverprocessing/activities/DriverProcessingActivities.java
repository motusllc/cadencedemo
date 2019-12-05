package com.motus.workflow.driverprocessing.activities;

import com.uber.cadence.activity.ActivityMethod;

public interface DriverProcessingActivities {

    DriverInfo getDriverInfo(String username);

    DriverInfo getBankAccountNumber(DriverInfo driver);

    void sendNachaFile(String fileContents);

    void updatePaymentStatus(String username);
}
