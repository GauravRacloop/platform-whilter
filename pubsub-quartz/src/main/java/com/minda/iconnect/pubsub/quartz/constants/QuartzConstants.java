package com.minda.iconnect.pubsub.quartz.constants;

/**
 * Created by mayank on 13/08/18 5:39 PM.
 */
public interface QuartzConstants {
        String INSTANCE_NAME = "org.quartz.scheduler.instanceName";
        String INSTANCE_ID = "org.quartz.scheduler.instanceId";
        String THREAD_COUNT = "org.quartz.threadPool.threadCount";
        String THREAD_POOL_CLASS = "org.quartz.threadPool.class";
        String JOB_STORE_CLASS = "org.quartz.jobStore.class";
        String DRIVER_DELEGATE_CLASS = "org.quartz.jobStore.driverDelegateClass";
        String TABLE_PREFIX = "org.quartz.jobStore.tablePrefix";
        String DATASOURCE = "org.quartz.jobStore.dataSource";
        String DATA_KEY = "data";
}
