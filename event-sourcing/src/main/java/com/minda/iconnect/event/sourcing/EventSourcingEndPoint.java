package com.minda.iconnect.event.sourcing;

import com.whilter.core.Endpoint;
/**
 * @author jaspreet on 15/01/19
 */
public class EventSourcingEndPoint implements Endpoint {

    private EventSourcing eventSourcing;

    public EventSourcing getEventSourcing() {
        return eventSourcing;
    }

    public void setEventSourcing(EventSourcing eventSourcing) {
        this.eventSourcing = eventSourcing;
    }
}
