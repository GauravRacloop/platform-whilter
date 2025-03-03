package com.minda.iconnect.saga;

import com.minda.iconnect.platform.core.Component;

public interface SagaComponent extends Component<SagaEndpoint, SagaService> {

    String APP = "saga";

}
