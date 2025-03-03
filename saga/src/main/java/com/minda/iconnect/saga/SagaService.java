package com.minda.iconnect.saga;

import com.minda.iconnect.platform.core.Service;

public interface SagaService extends Service {

    void publish(Change change);

    void register(String resourceType, Acceptor acceptor);

    interface Acceptor<T> {
        void changed(Change change);
    }
}
