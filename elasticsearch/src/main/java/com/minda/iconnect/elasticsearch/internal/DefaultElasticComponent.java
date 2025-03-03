package com.minda.iconnect.elasticsearch.internal;

import com.minda.iconnect.elasticsearch.Elastic;
import com.minda.iconnect.elasticsearch.ElasticComponent;
import com.minda.iconnect.elasticsearch.ElasticEndpoint;
import com.whilter.core.internal.AbstractComponent;

/**
 * Created by deepakchauhan on 23/07/17.
 */
public class DefaultElasticComponent extends AbstractComponent<ElasticEndpoint, Elastic>
        implements ElasticComponent {

    @Override
    protected Elastic doGet(ElasticEndpoint endpoint) {
        return new Elastic(endpoint);
    }
}
