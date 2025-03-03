package com.minda.iconnect.spark;

import com.minda.iconnect.platform.core.PlatformContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * Created by mayank on 25/08/17.
 */
public interface JobContext {

    JavaSparkContext getJavaSparkContext();

    SQLContext getSqlContext();

    JavaStreamingContext getStreamingContext();

    SparkSession getSparkSession();

    PlatformContext getPlatformContext();

}
