package com.minda.iconnect.iql;


public class RuleUnit {

    private String leftOperand;
    private Condition condition;
    private Object[] rightOperand;
    private Type type;

    public RuleUnit() {
    }

    public RuleUnit(String leftOperand, Condition condition, Object[] rightOperand, Type type) {
        this.leftOperand = leftOperand;
        this.condition = condition;
        this.rightOperand = rightOperand;
        this.type = type;
    }

    public String getLeftOperand() {
        return leftOperand;
    }

    public Condition getCondition() {
        return condition;
    }

    public Object[] getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(Object[] rightOperand) {
        this.rightOperand = rightOperand;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        FIELD, VALUE
    }
}
