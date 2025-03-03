package com.minda.iconnect.iql;

public class GroupBy {

    private String attribute;
    private String alias;
    private SlotType slotType;
    private Object value;
    private String displayName;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public void setSlotType(SlotType slotType) {
        this.slotType = slotType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public enum SlotType {
        TIME, NUMBER
    }

    public enum TimeSlot {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, HALFYEARLY, YEARLY
    }
}