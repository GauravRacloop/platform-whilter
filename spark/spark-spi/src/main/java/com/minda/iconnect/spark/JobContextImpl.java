package com.minda.iconnect.spark;

import com.minda.iconnect.platform.core.PlatformContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * Created by mayank on 04/09/17.
 */
public class JobContextImpl implements JobContext {

    private final JavaSparkContext javaSparkContext;
    private final SQLContext sqlContext;
    private final JavaStreamingContext streamingContext;
    private final SparkSession sparkSession;
    private final PlatformContext platformContext;

    public JobContextImpl(JavaSparkContext javaSparkContext, SQLContext sqlContext, JavaStreamingContext streamingContext, SparkSession sparkSession, PlatformContext platformContext) {
        this.javaSparkContext = javaSparkContext;
        this.sqlContext = sqlContext;
        this.streamingContext = streamingContext;
        this.sparkSession = sparkSession;
        this.platformContext = platformContext;
    }


    @Override
    public JavaSparkContext getJavaSparkContext() {
        return javaSparkContext;
    }

    @Override
    public SQLContext getSqlContext() {
        return sqlContext;
    }

    @Override
    public JavaStreamingContext getStreamingContext() {
        return streamingContext;
    }

    @Override
    public SparkSession getSparkSession() {
        return sparkSession;
    }

    @Override
    public PlatformContext getPlatformContext() {
        return platformContext;
    }
}
