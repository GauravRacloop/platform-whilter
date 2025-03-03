package com.minda.iconnect.spark;

/**
 * Created by mayank on 25/08/17.
 */
public interface SparkJob<T> {

    T executeJob();

}
