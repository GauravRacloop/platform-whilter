package com.minda.iconnect.spark.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * Created by mayank on 04/09/17.
 */
public class LivyBatches {

    private int from;
    private int total;

    @JsonProperty("sessions")
    private Collection<LivyBatch> batches;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Collection<LivyBatch> getBatches() {
        return batches;
    }

    public void setBatches(Collection<LivyBatch> batches) {
        this.batches = batches;
    }
}
