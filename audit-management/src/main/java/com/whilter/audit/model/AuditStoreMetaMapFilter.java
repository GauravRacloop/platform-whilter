package com.whilter.audit.model;

import lombok.Data;

import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.Map;

@Data
public class AuditStoreMetaMapFilter {

    @QueryParam("createdDateFrom")
    private Long createdDateFrom;
    @QueryParam("createdDateTo")
    private Long createdDateTo;
    @QueryParam("auditCategory")
    private String auditCategory;
    @QueryParam("offset")
    private int offset;
    @QueryParam("size")
    private int size;

}
