package com.minda.iconnect.saga.internal;

import com.minda.iconnect.platform.pubsub.SimpleConsumer;
import com.minda.iconnect.saga.Change;
import com.minda.iconnect.saga.SagaService;

import java.util.HashMap;
import java.util.Map;

public class SagaConsumer implements SimpleConsumer<Change> {

    private Map<String, SagaService.Acceptor> acceptorMap = new HashMap<>();

    protected void addAcceptor(String resourceType, SagaService.Acceptor acceptor) {
        acceptorMap.put(resourceType, acceptor);
    }

    @Override
    public void consume(Change message) {
        SagaService.Acceptor acceptor = acceptorMap.get(message.getResourceType());
        if (acceptor != null) {
            acceptor.changed(message);
        }
    }
}
