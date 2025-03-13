package com.whilter.pubsub.quartz.job;

import com.whilter.pubsub.Consumer;
import com.whilter.pubsub.Serializer;
import com.whilter.pubsub.SimpleConsumer;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

public class QuartzJobFactory implements JobFactory {

    private final SimpleConsumer consumer;
    private final Serializer serializer;

    public QuartzJobFactory(SimpleConsumer consumer, Serializer serializer) {
        this.consumer = consumer;
        this.serializer = serializer;
    }

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) {
        return new SimpleJob(consumer, serializer);
    }
}
