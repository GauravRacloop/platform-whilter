package com.minda.iconnect.spark;

import com.minda.iconnect.spark.param.Parameters;

/**
 * Created by mayank on 04/09/17.
 */
public abstract class AbstractBatchSparkJob extends AbstractSparkJob<Void> {

    public AbstractBatchSparkJob(JobContext jobContext, Parameters parameters) {
        super(jobContext, parameters);
    }

    @Override
    protected final Void doExecuteJob() {
        executeInternal();
        return null;
    }

    protected abstract void executeInternal();

}
