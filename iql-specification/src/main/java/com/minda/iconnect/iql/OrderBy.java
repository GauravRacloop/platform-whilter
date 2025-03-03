package com.minda.iconnect.iql;

public class OrderBy {

    private String field;
    private OrderType orderType;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }
}
