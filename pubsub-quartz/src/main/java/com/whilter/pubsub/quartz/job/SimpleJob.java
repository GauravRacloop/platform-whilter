package com.whilter.pubsub.quartz.job;

import com.whilter.pubsub.Serializer;
import com.whilter.pubsub.SimpleConsumer;
import com.whilter.pubsub.quartz.constants.QuartzConstants;
import org.quartz.Job;
import org.quartz.JobExecutionContext;


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
