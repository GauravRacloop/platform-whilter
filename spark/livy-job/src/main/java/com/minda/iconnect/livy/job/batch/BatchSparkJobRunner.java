package com.minda.iconnect.livy.job.batch;

import com.minda.iconnect.livy.job.JobContextFactory;
import com.minda.iconnect.livy.job.JobInfo;
import com.minda.iconnect.platform.core.PlatformContext;
import com.minda.iconnect.spark.JobContext;
import com.minda.iconnect.spark.JobContextImpl;
import com.minda.iconnect.spark.SparkJob;
import com.minda.iconnect.spark.param.Parameters;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

/**
 * Created by mayank on 04/09/17.
 */
public abstract class BatchSparkJobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchSparkJobRunner.class);

    public static final String JOB_ID = "jobId";

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf();
        SparkContext sc = new SparkContext(sparkConf);
        JavaSparkContext jsc = new JavaSparkContext(sc);

        try {
            JobParameters  jobParameters = new JobParameters();
            jobParameters.loadParameters(sparkConf);

            JobInfo jobInfo = new JobInfo().loadJobInfo(jobParameters.get(JOB_ID));

            PlatformContext platformContext = JobContextFactory.getPlatformContext(jobInfo.getSpringConfigClass());
            Parameters parameters = new Parameters();
            jobParameters.forEach(parameters::putIfAbsent);
            JobContext jobContext = new JobContextImpl(jsc, new SQLContext(sc), null, new SparkSession(sc), platformContext);

            Constructor<?> constructor = jobInfo.getJobClass().getConstructor(JobContext.class, Parameters.class);
            SparkJob<Void> instance = (SparkJob) constructor.newInstance(jobContext, parameters);
            instance.executeJob();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            jsc.stop();
            sc.stop();
            JobContextFactory.closeContext();
        }
    }

}
