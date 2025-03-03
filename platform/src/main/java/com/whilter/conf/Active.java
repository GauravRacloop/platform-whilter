package com.whilter.conf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.whilter.conf.internal.AbstractConfiguration;

public class Active extends AbstractConfiguration {

    private String[] values;

    @JsonCreator
    public Active(@JsonProperty("values") String[] values) {
        this.values = values;
    }

    @JsonValue
    public String[] getValues() {
        return values;
    }
}
