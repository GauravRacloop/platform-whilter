package com.whilter.audit.model;


import com.whilter.audit.dto.DeviceStatusAuditDto;
import com.whilter.audit.dto.VehicleTrackingAuditDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.HashMap;
import java.util.Map;

@Data
public class AuditStore {

    @Id
    private String id;
    @Indexed
    private Long createdDate;
    @Indexed
    private String auditCategory;
    @Indexed
    private Map<String, String> metaMap = new HashMap<>();

    private String createdBy;
    private Long updatedDate;
    private String updatedBy;

    private String description;
    private String message;
    private String entity;
    private EventType eventType = EventType.UPDATE;


    public enum EventType {
        CREATE, UPDATE, DELETE, READ;
    }

    public DeviceStatusAuditDto getDeviceStatusAuditDto() {
        DeviceStatusAuditDto deviceStatusAuditDto = new DeviceStatusAuditDto();
        deviceStatusAuditDto.setDeviceId(this.metaMap.get("deviceId"));
        deviceStatusAuditDto.setSerialNumber(this.metaMap.get("serialNumber"));
        deviceStatusAuditDto.setImeiNumber(this.metaMap.get("imeiNumber"));
        deviceStatusAuditDto.setOldStatus(this.metaMap.get("oldStatus"));
        deviceStatusAuditDto.setNewStatus(this.metaMap.get("newStatus"));
        deviceStatusAuditDto.setUpdatedDate(String.valueOf(this.createdDate));
        deviceStatusAuditDto.setUpdatedBy(this.metaMap.get("user"));
        return deviceStatusAuditDto;
    }

    public VehicleTrackingAuditDto getVehicleTrackingAuditDto() {
        VehicleTrackingAuditDto vehicleTrackingAuditDto = new VehicleTrackingAuditDto();
        vehicleTrackingAuditDto.setUserId(this.metaMap.get("userId"));
        vehicleTrackingAuditDto.setVehicleChassisNumber(this.metaMap.get("vehicleChassisNumber"));
        vehicleTrackingAuditDto.setOldStatus(this.metaMap.get("oldStatus"));
        vehicleTrackingAuditDto.setNewStatus(this.metaMap.get("newStatus"));
        vehicleTrackingAuditDto.setExpiry(this.metaMap.get("expiry"));
        vehicleTrackingAuditDto.setTrackingEndsOn(this.metaMap.get("trackingEndsOn"));
        vehicleTrackingAuditDto.setUpdatedDate(String.valueOf(this.createdDate));
        vehicleTrackingAuditDto.setUpdatedBy(this.metaMap.get("user"));
        return vehicleTrackingAuditDto;
    }

}
