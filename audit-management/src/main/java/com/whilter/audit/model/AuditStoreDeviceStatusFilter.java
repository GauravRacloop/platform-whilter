package com.whilter.audit.model;

import lombok.Data;

import javax.ws.rs.QueryParam;

@Data
public class AuditStoreDeviceStatusFilter {

    @QueryParam("createdDateFrom")
    private Long createdDateFrom;
    @QueryParam("createdDateTo")
    private Long createdDateTo;

    @QueryParam("oldStatus")
    private String oldStatus;
    @QueryParam("newStatus")
    private String newStatus;
    @QueryParam("deviceId")
    private String deviceId;
    @QueryParam("serialNumber")
    private String serialNumber;
    @QueryParam("imeiNumber")
    private String imeiNumber;
    @QueryParam("user")
    private String user;

    @QueryParam("offset")
    private int offset;
    @QueryParam("size")
    private int size;

}
