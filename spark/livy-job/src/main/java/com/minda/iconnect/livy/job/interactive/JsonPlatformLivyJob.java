package com.minda.iconnect.livy.job.interactive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minda.iconnect.spark.JobContext;
import com.minda.iconnect.spark.SparkJob;
import com.minda.iconnect.spark.param.Parameters;

import java.lang.reflect.Constructor;

/**
 * Created by mayank on 01/09/17.
 */
public class JsonPlatformLivyJob extends AbstractPlatformLivyJob<JsonResponse> {

    public JsonPlatformLivyJob(String jobId, Parameters parameters) {
        super(jobId, parameters);
    }

    @Override
    protected JsonResponse doCall(JobContext jobContext, Parameters parameters) throws Exception {
        Constructor<?> constructor = jobInfo.getJobClass().getConstructor(JobContext.class, Parameters.class);

        SparkJob<Object> instance = (SparkJob) constructor.newInstance(jobContext, parameters);

        Object value = instance.executeJob();

        return new JsonResponse(value.getClass().getName(), new ObjectMapper().writeValueAsString(value));
    }
}
