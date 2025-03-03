package com.minda.iconnect.jaxrs.jersey.client;

import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.jaxrs.JaxRSClient;
import com.minda.iconnect.platform.jaxrs.JaxRSClientComponent;
import com.minda.iconnect.platform.jaxrs.JaxRSEndpoint;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.WriterInterceptor;
import java.util.Map;

/**
 * Created by deepakchauhan on 14/07/17.
 */
public class JerseyClientComponent extends AbstractComponent<JaxRSEndpoint, JaxRSClient> implements JaxRSClientComponent {

    public static final ThreadLocal<ResourceInfo> RESOURCE_INFO_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    protected JaxRSClient doGet(JaxRSEndpoint endpoint) {
        ClientConfig clientConfig = new ClientConfig().connectorProvider(new GrizzlyConnectorProvider());
        Client client = ClientBuilder.newBuilder().withConfig(clientConfig).build();

        if (endpoint.getService().getLoadBalancer().getConnectionTimeout() != 0l) {
            client.property(ClientProperties.CONNECT_TIMEOUT, endpoint.getService().getLoadBalancer().getConnectionTimeout());
            client.property(ClientProperties.READ_TIMEOUT,    endpoint.getService().getLoadBalancer().getConnectionTimeout());
        }

        client.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.BUFFERED);

        for (Map.Entry<String, Object> entry : endpoint.getExtraProperties().entrySet()) {
            client.property(entry.getKey(), entry.getValue());
        }

        for (ReaderInterceptor interceptor : endpoint.getReaderInterceptors()) {
            client.register(interceptor);
        }

        for (WriterInterceptor interceptor : endpoint.getWriterInterceptors()) {
            client.register(interceptor);
        }

        if (endpoint.getFilters() != null) {
            for (Object filter : endpoint.getFilters()) {
                client.register(filter);
            }
        }

        if (endpoint.getService().getSecurityConf() != null) {
            //client.register(HttpAuthenticationFeature.basic("", "".getBytes()));
        }

        WebTarget webTarget = client.target(endpoint.getService().getLoadBalancer().getContextUrl());

        Object resource = WebResourceFactory.newResource(endpoint.getServiceType(), webTarget);
        return new JerseyClient<>(resource, webTarget);
    }

    @Override
    protected boolean cache() {
        return true;
    }

    @Override
    protected String toUniqueID(JaxRSEndpoint endpoint) {
        return endpoint.getService().getID();
    }
}
