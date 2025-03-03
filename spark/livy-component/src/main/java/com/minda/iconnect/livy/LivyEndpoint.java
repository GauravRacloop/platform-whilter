package com.minda.iconnect.livy;

import com.minda.iconnect.platform.core.Endpoint;
import com.minda.iconnect.spark.config.LivyConf;
import com.minda.iconnect.spark.config.SparkApplication;

/**
 * Created by deepakchauhan on 22/07/17.
 */
public class LivyEndpoint implements Endpoint {

    private LivyConf livyConf;
    private SparkApplication application;

    public LivyEndpoint(LivyConf livyConf, SparkApplication application) {
        this.livyConf = livyConf;
        this.application = application;
    }

    public LivyConf getLivyConf() {
        return livyConf;
    }

    public SparkApplication getApplication() {
        return application;
    }

    @Override
    public String toString() {
        return livyConf.getID() + '-' + application.getID();
    }
}
