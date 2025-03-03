package com.minda.iconnect.iql;

/**
 * Created by deepakchauhan on 07/08/17.
 */
public class PageRequest {

    private PaginationType paginationType;
    private int pageSize;
    private Object value;

    public PaginationType getPaginationType() {
        return paginationType;
    }

    public void setPaginationType(PaginationType paginationType) {
        this.paginationType = paginationType;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
