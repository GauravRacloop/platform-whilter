package com.minda.iconnect.iql;

public class Projection {

    private String field;
    private Function function;
    private String alias;
    private String displayName;

    public Projection(String field, Function function, String alias) {
        this.field = field;
        this.function = function;
        this.alias = alias;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
