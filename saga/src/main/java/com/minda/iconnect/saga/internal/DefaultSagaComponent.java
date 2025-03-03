package com.minda.iconnect.saga.internal;

import com.minda.iconnect.platform.core.ComponentResolver;
import com.minda.iconnect.platform.core.internal.AbstractCamelComponent;
import com.minda.iconnect.saga.SagaComponent;
import com.minda.iconnect.saga.SagaEndpoint;
import com.minda.iconnect.saga.SagaService;

public class DefaultSagaComponent extends AbstractCamelComponent<SagaEndpoint, SagaService> implements SagaComponent {

    @Override
    protected SagaService doGet(SagaEndpoint endpoint) {
        return new DefaultSagaService(this.getContext().get(ComponentResolver.class), endpoint);
    }

    @Override
    protected boolean cache() {
        return true;
    }

}
