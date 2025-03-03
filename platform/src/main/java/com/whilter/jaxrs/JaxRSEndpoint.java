package com.whilter.jaxrs;

import com.whilter.conf.ServiceConf;
import com.whilter.core.Endpoint;

import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.WriterInterceptor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by deepakchauhan on 16/07/17.
 */
public class JaxRSEndpoint implements Endpoint {

    private Class<?> serviceType;
    private ServiceConf service;
    private Object instance;
    private Object[] filters;
    private boolean enableResponseMapping;
    private Map<String, Object> extraProperties = new HashMap<>();

    private LinkedList<ReaderInterceptor> readerInterceptors = new LinkedList<>();
    private LinkedList<WriterInterceptor> writerInterceptors = new LinkedList<>();

    //For Client
    public JaxRSEndpoint(Class<?> serviceType, ServiceConf service) {
        this.serviceType = serviceType;
        this.service = service;
    }

    //For Server
    public JaxRSEndpoint(Class<?> serviceType, ServiceConf service, Object instance) {
        this.serviceType = serviceType;
        this.service = service;
        this.instance = instance;
    }

    public Class<?> getServiceType() {
        return serviceType;
    }

    public ServiceConf getService() {
        return service;
    }

    public Object getInstance() {
        return instance;
    }

    public void addReaderInterceptor(ReaderInterceptor interceptor) {
        readerInterceptors.add(interceptor);
    }

    public Iterable<ReaderInterceptor> getReaderInterceptors() {
        return readerInterceptors;
    }

    public void addWriterInterceptor(WriterInterceptor interceptor) {
        writerInterceptors.add(interceptor);
    }

    public LinkedList<WriterInterceptor> getWriterInterceptors() {
        return writerInterceptors;
    }

    public Object[] getFilters() {
        return filters;
    }

    public void setFilters(Object[] filters) {
        this.filters = filters;
    }

    public boolean isResponseMappingEnabled() {
        return enableResponseMapping;
    }

    public void enableResponseMapping(boolean value) {
        this.enableResponseMapping = value;
    }

    public void addProperty(String propName, Object propValue) {
        extraProperties.put(propName, propValue);
    }

    public Object getProperty(String propKey) {
        return  extraProperties.get(propKey);
    }

    public Map<String, Object> getExtraProperties() {
        return extraProperties;
    }
}
