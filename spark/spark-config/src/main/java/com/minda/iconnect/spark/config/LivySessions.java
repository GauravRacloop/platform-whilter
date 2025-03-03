package com.minda.iconnect.spark.config;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

/**
 * Created by mayank on 28/08/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LivySessions {
    private int from;
    private int total;
    private Collection<LivySession> sessions;

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

    public Collection<LivySession> getSessions() {
        return sessions;
    }

    public void setSessions(Collection<LivySession> sessions) {
        this.sessions = sessions;
    }
}
