package com.minda.iconnect.livy.internal;

import com.minda.iconnect.livy.Livy;
import com.minda.iconnect.livy.LivyComponent;
import com.minda.iconnect.livy.LivyEndpoint;
import com.minda.iconnect.livy.LivyRestApi;
import com.minda.iconnect.platform.conf.ConfigReader;
import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.spark.config.SparkJobsDistro;

/**
 * Created by deepakchauhan on 22/07/17.
 */
public class DefaultLivyComponent extends AbstractComponent<LivyEndpoint, Livy> implements LivyComponent {

    @Override
    protected Livy doGet(LivyEndpoint endpoint) {
        return new LivyAdapter(endpoint,
                getContext().get(ConfigReader.class).read("spark-distribution", SparkJobsDistro.class),
                getContext().get("livyRestApiClient", LivyRestApi.class));
    }

    @Override
    protected String toUniqueID(LivyEndpoint endpoint) {
        return endpoint.toString();
    }

    @Override
    protected boolean cache() {
        return true;
    }
}
