package com.minda.iconnect.pubsub.quartz.job;

import com.minda.iconnect.platform.pubsub.Consumer;
import com.minda.iconnect.platform.pubsub.Serializer;
import com.minda.iconnect.platform.pubsub.SimpleConsumer;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import java.util.Collection;

/**
 * Created by mayank on 13/08/18 1:53 PM.
 */
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
