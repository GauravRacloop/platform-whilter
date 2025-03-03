package com.minda.iconnect.jaxrs.jersey.client;

import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.platform.jaxrs.JaxRSClient;

import javax.ws.rs.client.WebTarget;
import java.io.IOException;

/**
 * Created by deepakchauhan on 15/07/17.
 */
public class JerseyClient<T> extends AbstractService implements JaxRSClient<T> {

    private T proxy;
    private WebTarget webTarget;

    public JerseyClient(T proxy, WebTarget webTarget) {
        this.proxy = proxy;
        this.webTarget = webTarget;
    }

    @Override
    public T proxy() {
        return proxy;
    }

    @Override
    public WebTarget target() {
        return webTarget;
    }

    @Override
    protected void doStart() throws IOException {
        //Nothing to do
    }

    @Override
    protected void doShutdown() {
        //Nothing to do
    }
}
