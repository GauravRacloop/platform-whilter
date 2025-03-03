package com.minda.iconnect.iql;

import java.util.Collection;
import java.util.List;

public class Query {

    private String source;
    private String model;
    private LexFilter lexFilter;
    private List<Projection> projections;
    private List<GroupBy> groupAttributes;
    private Collection<OrderBy> orderBy;
    private PageRequest pageRequest;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LexFilter getLexFilter() {
        return lexFilter;
    }

    public void setLexFilter(LexFilter lexFilter) {
        this.lexFilter = lexFilter;
    }

    public List<Projection> getProjections() {
        return projections;
    }

    public void setProjections(List<Projection> projections) {
        this.projections = projections;
    }

    public List<GroupBy> getGroupAttributes() {
        return groupAttributes;
    }

    public void setGroupAttributes(List<GroupBy> groupAttributes) {
        this.groupAttributes = groupAttributes;
    }

    public Collection<OrderBy> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Collection<OrderBy> orderBy) {
        this.orderBy = orderBy;
    }

    public PageRequest getPageRequest() {
        return pageRequest;
    }

    public void setPageRequest(PageRequest pageRequest) {
        this.pageRequest = pageRequest;
    }
}
