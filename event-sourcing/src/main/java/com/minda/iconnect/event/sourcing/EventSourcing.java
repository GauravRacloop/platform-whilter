package com.minda.iconnect.event.sourcing;

import com.minda.iconnect.platform.conf.ConfArray;
import com.minda.iconnect.platform.conf.internal.AbstractConfiguration;
import com.whilter.core.Endpoint;
import com.whilter.pubsub.PubsubEndpoint;

/**
 * @author jaspreet on 16/01/19
 */
@ConfArray(EventSourcing[].class)
public class EventSourcing extends AbstractConfiguration implements Endpoint {

    private PubsubEndpoint c2d;
    private PubsubEndpoint delete;
    private String stateStore;
    private QueryType queryType;

    public PubsubEndpoint getC2d() {
        return c2d;
    }

    public void setC2d(PubsubEndpoint c2d) {
        this.c2d = c2d;
    }

    public PubsubEndpoint getDelete() {
        return delete;
    }

    public void setDelete(PubsubEndpoint delete) {
        this.delete = delete;
    }

    public String getStateStore() {
        return stateStore;
    }

    public void setStateStore(String stateStore) {
        this.stateStore = stateStore;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public enum QueryType{
        KEY,RANGE
    }
}
