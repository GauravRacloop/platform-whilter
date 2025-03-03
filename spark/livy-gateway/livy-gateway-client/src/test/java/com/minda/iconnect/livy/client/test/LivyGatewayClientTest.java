package com.minda.iconnect.livy.client.test;

import com.minda.iconnect.lgclient.JsonReaderInterceptor;
import com.minda.iconnect.livy.gateway.api.LivyGatewayApi;
import com.minda.iconnect.livy.gateway.api.Parameters;
import com.minda.iconnect.platform.AbstractSpringConfig;
import com.minda.iconnect.platform.PlatformConfig;
import com.minda.iconnect.platform.conf.ServiceConf;
import com.minda.iconnect.platform.core.Component;
import com.minda.iconnect.platform.core.ComponentResolver;
import com.minda.iconnect.platform.jaxrs.JaxRSClient;
import com.minda.iconnect.platform.jaxrs.JaxRSClientComponent;
import com.minda.iconnect.platform.jaxrs.JaxRSEndpoint;
import com.minda.iconnect.platform.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.inject.Named;

/**
 * Created by mayank on 05/09/17.
 */

@Import(PlatformConfig.class)
public class LivyGatewayClientTest extends AbstractSpringConfig {

    public static void main(String[] args) throws Exception {
        Runner.main(new String[]{"--spring", "com.minda.iconnect.livy.client.test.LivyGatewayClientTest"});
    }

    @Autowired
    @SuppressWarnings("Duplicates")
    public void lgApiClient(ComponentResolver resolver, @Named("livy-gateway") ServiceConf conf) throws Exception {
        Component<JaxRSEndpoint, JaxRSClient> component = resolver.resolveAny(JaxRSClientComponent.class);
        JaxRSEndpoint jaxRSEndpoint = new JaxRSEndpoint(LivyGatewayApi.class, conf);
        jaxRSEndpoint.addReaderInterceptor(new JsonReaderInterceptor());

        if (jaxRSEndpoint.getService().getID() == null) {
            jaxRSEndpoint.getService().setID(conf.getID() + "livy-gateway-api-client");
        }

        JaxRSClient<LivyGatewayApi> rsClient = component.get(jaxRSEndpoint);

        Parameters parameters = new Parameters();
        parameters.put("1", "1");
        parameters.put("2", "2");
        parameters.put("3", "3");
        parameters.put("4", "4");
        parameters.put("5", "5");
        rsClient.proxy().executeBatch("driving-batch", parameters);
        System.exit(0);
    }
}
