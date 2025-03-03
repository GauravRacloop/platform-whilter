package com.whilter.core.internal;


import com.whilter.core.CamelComponent;
import com.whilter.core.Endpoint;
import com.whilter.core.Service;
import org.apache.camel.CamelContext;

/**
 * Created by deepakchauhan on 16/07/17.
 */
public abstract class AbstractCamelComponent<E extends Endpoint, S extends Service> extends AbstractComponent<E, S>
        implements CamelComponent<E, S> {

    protected CamelContext camelContext;

    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
}
