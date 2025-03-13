package com.whilter.audit.dto;

import lombok.Data;

@Data
public class VehicleTrackingAuditDto {

    private String userId;
    private String vehicleChassisNumber;
    private String expiry;
    private String trackingEndsOn;
    private String oldStatus;
    private String newStatus;
    private String updatedDate;
    private String updatedBy;
}
