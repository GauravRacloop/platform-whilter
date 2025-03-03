package com.whilter.conf;

import com.whilter.conf.internal.AbstractConfiguration;

import java.util.List;

/**
 * Created by deepakchauhan on 15/07/17.
 */
@ConfArray(SecurityConf[].class)
public class SecurityConf extends AbstractConfiguration {

    private String httpScheme = "http";

    private AuthType[] allowedAuthTypes;

    private List<String> sources;

    private String keyStore;

    private String keyStorePass;

    private String trustStore;

    private String trustStorePass;

    public String getHttpScheme() {
        return httpScheme;
    }

    public void setHttpScheme(String httpScheme) {
        this.httpScheme = httpScheme;
    }

    public AuthType[] getAllowedAuthTypes() {
        return allowedAuthTypes;
    }

    public void setAllowedAuthTypes(AuthType[] allowedAuthTypes) {
        this.allowedAuthTypes = allowedAuthTypes;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStorePass() {
        return keyStorePass;
    }

    public void setKeyStorePass(String keyStorePass) {
        this.keyStorePass = keyStorePass;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePass() {
        return trustStorePass;
    }

    public void setTrustStorePass(String trustStorePass) {
        this.trustStorePass = trustStorePass;
    }

    public enum AuthType {
        BASIC, FORM, OAUTH2, OPENID, API_KEY
    }
}
