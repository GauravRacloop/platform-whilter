package com.whilter.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.whilter.conf.internal.AbstractConfiguration;

/**
 * Created by deepakchauhan on 24/05/17.
 */
@JsonRootName("service")
@ConfArray(ServiceConf[].class)
public class ServiceConf extends AbstractConfiguration {

    @JsonProperty("servers")
    private HTTPServer[] hosts;

    private HTTPServer loadBalancer;

    private boolean deployedOnContainer;

    private boolean gzipEnable = true;

    @JsonProperty("security")
    private SecurityConf securityConf;

    private boolean container;

    public HTTPServer[] getHosts() {
        return hosts;
    }

    public void setHosts(HTTPServer[] hosts) {
        this.hosts = hosts;
    }

    public boolean isDeployedOnContainer() {
        return deployedOnContainer;
    }

    public void setDeployedOnContainer(boolean deployedOnContainer) {
        this.deployedOnContainer = deployedOnContainer;
    }

    public HTTPServer getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(HTTPServer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public boolean isGzipEnable() {
        return gzipEnable;
    }

    public void setGzipEnable(boolean gzipEnable) {
        this.gzipEnable = gzipEnable;
    }

    public SecurityConf getSecurityConf() {
        return securityConf;
    }

    public void setSecurityConf(SecurityConf securityConf) {
        this.securityConf = securityConf;
    }

    public boolean isContainer() {
        return container;
    }

    public void setContainer(boolean container) {
        this.container = container;
    }
}
