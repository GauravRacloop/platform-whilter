package com.whilter.jaxrs;

import com.whilter.core.Service;

import javax.ws.rs.client.WebTarget;

/**
 * Created by deepakchauhan on 15/07/17.
 */
public interface JaxRSClient<T> extends Service {

    T proxy();

    WebTarget target();

}
