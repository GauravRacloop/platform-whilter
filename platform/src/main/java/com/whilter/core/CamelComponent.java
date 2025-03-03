package com.whilter.core;

import org.apache.camel.CamelContext;

/**
 * Created by deepakchauhan on 16/07/17.
 */
public interface CamelComponent<E extends Endpoint, S extends Service> extends Component<E, S> {

    CamelContext getCamelContext();
}
