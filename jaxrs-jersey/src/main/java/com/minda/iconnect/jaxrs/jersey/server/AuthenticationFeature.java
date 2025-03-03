package com.minda.iconnect.jaxrs.jersey.server;

import com.minda.iconnect.platform.security.ApiAuth;
import com.minda.iconnect.platform.security.GuestApi;
import io.dropwizard.auth.AuthFilter;
import org.glassfish.jersey.server.model.AnnotatedMethod;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.FeatureContext;

@Priority(Priorities.AUTHENTICATION - 100)
public class AuthenticationFeature implements DynamicFeature, ContainerRequestFilter {

    public static final ThreadLocal<ResourceInfo> RESOURCE_INFO_THREAD_LOCAL = new ThreadLocal<>();

    private AuthFilter filter;
    private AuthFilter apiKeyFilter;

    public AuthenticationFeature(AuthFilter authFilter, AuthFilter apiKeyFilter) {
        this.filter = authFilter;
        this.apiKeyFilter = apiKeyFilter;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        final AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());
        if (!am.isAnnotationPresent(GuestApi.class)) {
            if (am.isAnnotationPresent(ApiAuth.class)) {
                context.register(apiKeyFilter);
            } else {
                context.register(filter);
            }
        }
    }

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        RESOURCE_INFO_THREAD_LOCAL.set(resourceInfo);
    }
}
