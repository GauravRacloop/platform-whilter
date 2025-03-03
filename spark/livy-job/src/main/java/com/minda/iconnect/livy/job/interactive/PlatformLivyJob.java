package com.minda.iconnect.livy.job.interactive;

import com.minda.iconnect.spark.JobContext;
import com.minda.iconnect.spark.SparkJob;
import com.minda.iconnect.spark.param.Parameters;

import java.lang.reflect.Constructor;

/**
 * Created by deepakchauhan on 22/07/17.
 */
public class PlatformLivyJob<T> extends AbstractPlatformLivyJob<T> {


    public PlatformLivyJob(String jobId, Parameters parameters) {
        super(jobId, parameters);
    }

    @Override
    protected T doCall(JobContext jobContext, Parameters parameters) throws Exception {
        Constructor<?> constructor = jobInfo.getJobClass().getConstructor(JobContext.class, Parameters.class);
        SparkJob<T> instance = (SparkJob) constructor.newInstance(jobContext, parameters);
        return instance.executeJob();
    }
}