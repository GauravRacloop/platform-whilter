package com.minda.iconnect.jaxrs.jersey.server;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

/**
 * @author mayank on 04/02/20 11:29 AM
 */
public class ApiKeyCredentials implements Principal {
    private String apiName;
    private String apiToken;
    private Map<String, String> properties;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String getName() {
        return apiName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiKeyCredentials that = (ApiKeyCredentials) o;
        return Objects.equals(apiName, that.apiName) &&
                Objects.equals(apiToken, that.apiToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiName, apiToken);
    }
}
