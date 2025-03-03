package com.minda.iconnect.livy.gateway.engine;

import com.minda.iconnect.livy.LivyRestApi;
import com.minda.iconnect.spark.config.LivyBatch;
import com.minda.iconnect.spark.config.LivyConf;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by mayank on 18/09/17.
 */
public class BatchPurgeService implements Runnable {

    private LivyConf livyConf;
    private LivyRestApi livyRestApi;

    public BatchPurgeService(LivyRestApi livyRestApi, LivyConf livyConf) {
        this.livyRestApi = livyRestApi;
        this.livyConf = livyConf;
    }

    @PostConstruct
    public void init() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this, 10000, livyConf.getBatchPurgeInterval(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        Collection<LivyBatch> batches = livyRestApi.getAllBatches().getBatches();

        if (batches != null) {
            batches.stream()
                    .filter(batch -> !batch.getState().equals("running"))
                    .forEach(batch -> livyRestApi.deleteBatch(batch.getId()));
        }
    }
}
