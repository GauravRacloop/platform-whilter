package com.minda.iconnect.jaxrs.jersey.client;

import java.lang.reflect.Method;

/**
 * @author mayank on 10/07/20 6:24 PM
 */
public class ResourceInfo {

    private Object proxy;
    private Method method;
    private Object[] args;

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
