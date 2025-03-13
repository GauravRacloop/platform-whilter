package com.whilter.audit.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AuditStoreFilter {

    private Long createdDateFrom;
    private Long createdDateTo;
    private String auditCategory;
    private Map<String, String> metaMap = new HashMap<>();
    private int offset;
    private int size;

    public AuditStoreFilter() {
    }

    public AuditStoreFilter(AuditStoreMetaMapFilter auditStoreMetaMapFilter) {
        this.createdDateFrom = auditStoreMetaMapFilter.getCreatedDateFrom();
        this.createdDateTo = auditStoreMetaMapFilter.getCreatedDateTo();
        this.auditCategory = auditStoreMetaMapFilter.getAuditCategory();
        this.offset = auditStoreMetaMapFilter.getOffset();
        this.size = auditStoreMetaMapFilter.getSize();
    }
}
