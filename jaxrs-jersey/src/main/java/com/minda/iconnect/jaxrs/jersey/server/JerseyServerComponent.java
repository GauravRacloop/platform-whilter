package com.minda.iconnect.jaxrs.jersey.server;

import com.minda.iconnect.jaxrs.jersey.server.security.AuthenticationExceptionMapper;
import com.minda.iconnect.platform.conf.HTTPServer;
import com.minda.iconnect.platform.conf.SecurityConf;
import com.minda.iconnect.platform.conf.ServiceConf;
import com.minda.iconnect.platform.core.Service;
import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.jaxrs.JaxRSEndpoint;
import com.minda.iconnect.platform.jaxrs.JaxRSServerComponent;
import com.minda.iconnect.platform.util.NetworkUtil;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.CarotGrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.springframework.util.Base64Utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by deepakchauhan on 14/07/17.
 */
public class JerseyServerComponent extends AbstractComponent<JaxRSEndpoint, Service> implements JaxRSServerComponent {

    private static Map<Integer, JerseyServer> serverMap = new HashMap<>();
    private Object lock = new Object();

    @Override
    protected Service doGet(JaxRSEndpoint endpoint) {
        ResourceConfig resourceConfig = new ResourceConfig(JacksonFeature.class);
        resourceConfig.registerInstances(endpoint.getInstance());
        resourceConfig.register(MultiPartFeature.class);

        if (endpoint.isResponseMappingEnabled()) {
            resourceConfig.register(JerseyExceptionMapper.class);
        }

        if (endpoint.getFilters() != null && endpoint.getFilters().length > 0) {
            for (Object filter : endpoint.getFilters()) {
                resourceConfig.register(filter);
            }
        }

        if (endpoint.getService().getSecurityConf() != null && endpoint.getService().getSecurityConf().getAllowedAuthTypes() != null) {
            resourceConfig.registerClasses(AuthenticationExceptionMapper.class);
            SecurityConf securityConf = endpoint.getService().getSecurityConf();

            List<AuthFilter> authFilters = new ArrayList<>();
            AuthFilter apiKeyFilter = null;
            for (SecurityConf.AuthType authType : securityConf.getAllowedAuthTypes()) {
                switch (authType) {
                    case OAUTH2:
                        authFilters.add(new OAuthCredentialAuthFilter.Builder<>()
                                .setAuthenticator(getContext().get(SecurityConf.AuthType.OAUTH2 + "Authenticator", Authenticator.class))
                                .setPrefix("Bearer")
                                .buildAuthFilter());
                        break;
                    case BASIC:
                        authFilters.add(new BasicCredentialAuthFilter.Builder<>()
                                .setAuthenticator(getContext().get(SecurityConf.AuthType.BASIC + "Authenticator", Authenticator.class))
                                .setPrefix("Basic")
                                .buildAuthFilter());
                        break;
                    case API_KEY:
                        apiKeyFilter = new ApiKeyCredentialsAuthFilter.Builder<>()
                                .setAuthenticator(getContext().get(SecurityConf.AuthType.API_KEY + "Authenticator", Authenticator.class))
                                .setPrefix("ApiKey")
                                .buildAuthFilter();
                    default:
                }
            }

            resourceConfig.register(new AuthenticationFeature(new ChainedAuthFilter(authFilters), apiKeyFilter));
            resourceConfig.register(RolesAllowedDynamicFeature.class);
        }

        if (endpoint.getService().isContainer()) {
            new ContainerFactory().configure(endpoint, resourceConfig);
            return new JerseyServer(null);
        } else {
            HTTPServer host;
            try {
                host = getHost(endpoint.getService());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            if (host == null) {
                throw new IllegalArgumentException("No service is configured for hosted server for service: " +
                        endpoint.getServiceType().getCanonicalName());
            }
            JerseyServer jServer = serverMap.get(host.getPort());

            SSLEngineConfigurator ssl = getSSLConfigurator(host, endpoint);

            URI uri = URI.create(host.getContextUrl());
            if (jServer == null) {
                synchronized (lock) {
                    jServer = serverMap.get(host.getPort());
                    if (jServer == null) {
                        HttpServer server = new CarotGrizzlyHttpServerFactory(null).createHttpServer(uri, resourceConfig, ssl);
                        jServer = new JerseyServer(server);
                        serverMap.put(host.getPort(), jServer);
                    }
                }
            } else {
                new CarotGrizzlyHttpServerFactory(jServer.httpServer).createHttpServer(uri, resourceConfig, ssl);
            }
            return jServer;
        }
    }

    private SSLEngineConfigurator getSSLConfigurator(HTTPServer host, JaxRSEndpoint endpoint) {
        SSLEngineConfigurator ssl = null;
        if (host.getScheme().equalsIgnoreCase("https")) {
            if (endpoint.getService() != null && endpoint.getService().getSecurityConf() != null) {
                SecurityConf securityConf = endpoint.getService().getSecurityConf();

                if (securityConf.getKeyStore() == null || securityConf.getKeyStore().trim().isEmpty()) {
                    throw new IllegalArgumentException("Invalid KeyStore");
                }

                if (securityConf.getKeyStorePass() == null || securityConf.getKeyStorePass().trim().isEmpty()) {
                    throw new IllegalArgumentException("Invalid KeyStorePassword");
                }

                if (securityConf.getTrustStore() == null || securityConf.getTrustStore().trim().isEmpty()) {
                    throw new IllegalArgumentException("Invalid TrustStore");
                }

                if (securityConf.getTrustStorePass() == null || securityConf.getTrustStorePass().trim().isEmpty()) {
                    throw new IllegalArgumentException("Invalid TrustStorePassword");
                }

                byte[] keyStoreBytes;
                try {
                    keyStoreBytes = Base64Utils.decode(securityConf.getKeyStore().getBytes());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid KeyStore");
                }
                byte[] trustStoreBytes;

                try {
                    trustStoreBytes = Base64Utils.decode(securityConf.getTrustStore().getBytes());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid TrustStore");
                }

                SSLContextConfigurator sslCon = new SSLContextConfigurator();
                sslCon.setKeyStoreBytes(keyStoreBytes);
                sslCon.setKeyStorePass(securityConf.getKeyStorePass());

                sslCon.setTrustStoreBytes(trustStoreBytes);
                sslCon.setTrustStorePass(securityConf.getTrustStorePass());

                ssl = new SSLEngineConfigurator(sslCon).setClientMode(false).setNeedClientAuth(false);
            }
        }
        return ssl;
    }

    private HTTPServer getHost(ServiceConf service) throws Exception {
        for (HTTPServer server : service.getHosts()) {
            if (NetworkUtil.isThisSever(server.getHost())) {
                return server;
            }
        }
        return null;
    }
}
