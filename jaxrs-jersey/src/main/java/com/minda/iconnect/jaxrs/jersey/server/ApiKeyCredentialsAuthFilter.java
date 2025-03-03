package com.minda.iconnect.jaxrs.jersey.server;

import com.minda.iconnect.platform.security.ApiAuth;
import io.dropwizard.auth.AuthFilter;
import org.glassfish.jersey.server.model.AnnotatedMethod;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import java.lang.annotation.Annotation;
import java.security.Principal;

@Priority(Priorities.AUTHENTICATION)
public class ApiKeyCredentialsAuthFilter<P extends Principal> extends AuthFilter<ApiKeyCredentials, P> {

    private static final String OAUTH_ACCESS_TOKEN_PARAM = "api_access_token";
    private static final String[] SECONDARY_METHODS = {"bearer", "basic"};

    private ApiKeyCredentialsAuthFilter() {
    }

    <T extends Annotation> T getAnnotation(Class<T> clazz) {
        ResourceInfo resourceInfo = AuthenticationFeature.RESOURCE_INFO_THREAD_LOCAL.get();
        final AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());
        if (am.isAnnotationPresent(clazz)) {
            T annotation = am.getAnnotation(clazz);
            return annotation;
        }
        return null;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        ApiAuth apiAuth = getAnnotation(ApiAuth.class);

        if (apiAuth == null) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        }

        String apiToken = fetchCredentials(requestContext.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

        // If Authorization header is not used, check query parameter where token can be passed as well
        if (apiToken == null) {
            apiToken = requestContext.getUriInfo().getQueryParameters().getFirst(OAUTH_ACCESS_TOKEN_PARAM);
        }

        ApiKeyCredentials apiKeyCredentials = new ApiKeyCredentials();
        apiKeyCredentials.setApiToken(apiToken);
        apiKeyCredentials.setApiName(apiAuth.apiName());

        if (!authenticate(requestContext, apiKeyCredentials, SecurityContext.BASIC_AUTH)) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
        }
    }

    private String fetchCredentials(String header) {
        if (header == null) {
            return null;
        }

        final int space = header.indexOf(' ');
        if (space <= 0) {
            return null;
        }

        final String method = header.substring(0, space);
        if (!prefix.equalsIgnoreCase(method)) {
            boolean found = false;
            for (String secondaryMethod : SECONDARY_METHODS) {
                if (method.equalsIgnoreCase(secondaryMethod)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return null;
            }
        }

        return header.substring(space + 1);
    }

    public static class Builder<P extends Principal> extends AuthFilterBuilder<ApiKeyCredentials, P, ApiKeyCredentialsAuthFilter<P>> {

        @Override
        protected ApiKeyCredentialsAuthFilter<P> newInstance() {
            return new ApiKeyCredentialsAuthFilter<>();
        }
    }
}
