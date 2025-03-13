package com.whilter.audit.model;

import lombok.Data;

import javax.ws.rs.QueryParam;

@Data
public class AuditStoreVehicleTrackingFilter {

    @QueryParam("createdDateFrom")
    private Long createdDateFrom;
    @QueryParam("createdDateTo")
    private Long createdDateTo;

    @QueryParam("oldStatus")
    private String oldStatus;
    @QueryParam("newStatus")
    private String newStatus;
    @QueryParam("userId")
    private String userId;
    @QueryParam("vehicleChassisNumber")
    private String vehicleChassisNumber;
    @QueryParam("sessionId")
    private String sessionId;
    @QueryParam("user")
    private String user;

    @QueryParam("offset")
    private int offset;
    @QueryParam("size")
    private int size;

}
