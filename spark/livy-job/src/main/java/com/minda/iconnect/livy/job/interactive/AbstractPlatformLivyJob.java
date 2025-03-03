package com.minda.iconnect.livy.job.interactive;

import com.cloudera.livy.Job;
import com.minda.iconnect.livy.job.JobContextFactory;
import com.minda.iconnect.livy.job.JobInfo;
import com.minda.iconnect.platform.core.PlatformContext;
import com.minda.iconnect.spark.JobContext;
import com.minda.iconnect.spark.JobContextImpl;
import com.minda.iconnect.spark.param.Parameters;

/**
 * Created by mayank on 01/09/17.
 */
public abstract class AbstractPlatformLivyJob<T> implements Job<T> {

    protected final String jobId;
    protected final Parameters parameters;
    protected final JobInfo jobInfo;

    public AbstractPlatformLivyJob(String jobId, Parameters parameters) {
        this.jobId = jobId;
        this.parameters = parameters;
        this.jobInfo = new JobInfo();
    }

    @Override
    public T call(com.cloudera.livy.JobContext jc) throws Exception {
        jobInfo.loadJobInfo(jobId);

        JobContext jobContext = provideJobContext(jc);
        return doCall(jobContext, parameters);
    }

    protected abstract T doCall(JobContext jobContext, Parameters parameters) throws Exception;


    protected JobContext provideJobContext(com.cloudera.livy.JobContext jc) throws Exception {
        Class<?> springConfig = jobInfo.getSpringConfigClass();
        PlatformContext platformContext = JobContextFactory.getPlatformContext(springConfig);
        return new JobContextImpl(
                jc.sc(),
                jc.sqlctx(),
                jobInfo.isStreamingJob() ? jc.streamingctx() : null,
                jc.sparkSession(),
                platformContext);
    }

}
