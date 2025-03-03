package com.minda.iconnect.spark;

import com.minda.iconnect.spark.param.Parameters;

/**
 * Created by mayank on 25/08/17.
 */
public abstract class AbstractSparkJob<T> implements SparkJob<T> {

    protected final JobContext jobContext;
    protected final Parameters parameters;

    public AbstractSparkJob(JobContext jobContext, Parameters parameters) {
        this.jobContext = jobContext;
        this.parameters = parameters;
    }

    @Override
    public final T executeJob() {
        return doExecuteJob();
    }

    protected abstract T doExecuteJob();
}
