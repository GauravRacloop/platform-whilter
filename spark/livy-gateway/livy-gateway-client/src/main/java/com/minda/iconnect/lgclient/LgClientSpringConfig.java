package com.minda.iconnect.lgclient;

import com.minda.iconnect.livy.gateway.api.LivyGatewayApi;
import com.minda.iconnect.platform.AbstractSpringConfig;
import com.minda.iconnect.platform.PlatformConfig;
import com.minda.iconnect.platform.conf.ConfigReader;
import com.minda.iconnect.platform.conf.ServiceConf;
import com.minda.iconnect.platform.core.Component;
import com.minda.iconnect.platform.core.ComponentResolver;
import com.minda.iconnect.platform.jaxrs.JaxRSClient;
import com.minda.iconnect.platform.jaxrs.JaxRSClientComponent;
import com.minda.iconnect.platform.jaxrs.JaxRSEndpoint;
import com.minda.iconnect.platform.runner.Runner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.inject.Named;

/**
 * Created by mayank on 31/08/17.
 */
@Import(PlatformConfig.class)
public class LgClientSpringConfig extends AbstractSpringConfig {

    @Bean("livy-gateway-api-client")
    public LivyGatewayApi lgApiClient(ComponentResolver resolver, @Named("livy-gateway") ServiceConf conf) throws Exception {
        Component<JaxRSEndpoint, JaxRSClient> component = resolver.resolveAny(JaxRSClientComponent.class);
        JaxRSEndpoint jaxRSEndpoint = new JaxRSEndpoint(LivyGatewayApi.class, conf);
        jaxRSEndpoint.addReaderInterceptor(new JsonReaderInterceptor());

        if (jaxRSEndpoint.getService().getID() == null) {
            jaxRSEndpoint.getService().setID(conf.getID() + "livy-gateway-api-client");
        }

        JaxRSClient<LivyGatewayApi> rsClient = component.get(jaxRSEndpoint);

        return rsClient.proxy();
    }
}
