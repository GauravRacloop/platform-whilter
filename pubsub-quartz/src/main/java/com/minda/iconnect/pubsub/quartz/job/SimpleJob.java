package com.minda.iconnect.pubsub.quartz.job;

import com.minda.iconnect.platform.pubsub.Consumer;
import com.minda.iconnect.platform.pubsub.Serializer;
import com.minda.iconnect.platform.pubsub.SimpleConsumer;
import com.minda.iconnect.pubsub.quartz.constants.QuartzConstants;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Collection;

/**
 * Created by mayank on 13/08/18 1:53 PM.
 */
public class SimpleJob implements Job, QuartzConstants {

    private final SimpleConsumer consumer;
    private final Serializer serializer;

    public SimpleJob(SimpleConsumer consumer, Serializer serializer) {
        this.consumer = consumer;
        this.serializer = serializer;
    }

    @Override
    public void execute(JobExecutionContext context) {
        byte[] data = (byte[]) context.getJobDetail().getJobDataMap().get(DATA_KEY);
        consumer.consume(serializer.deserialize(data));
    }
}
