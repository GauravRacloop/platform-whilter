package com.whilter.audit.dto;

import lombok.Data;

@Data
public class DeviceStatusAuditDto {

    private String deviceId;
    private String serialNumber;
    private String imeiNumber;
    private String oldStatus;
    private String newStatus;
    private String updatedDate;
    private String updatedBy;
}
