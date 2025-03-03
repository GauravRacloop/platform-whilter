package com.minda.iconnect.pubsub.quartz.subscriber;

import com.minda.iconnect.platform.core.PlatformContext;
import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.platform.pubsub.*;
import com.minda.iconnect.platform.rdbms.RDBMS;
import com.minda.iconnect.pubsub.quartz.QuartzConf;
import com.minda.iconnect.pubsub.quartz.constants.QuartzConstants;
import com.minda.iconnect.pubsub.quartz.job.QuartzJobFactory;
import com.minda.iconnect.pubsub.quartz.ds.ConnectionProviderImpl;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.jdbcjobstore.JobStoreTX;
import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.utils.DBConnectionManager;

import javax.sql.DataSource;
import java.util.*;

/**
 * Created by mayank on 13/08/18 12:17 PM.
 */
public class QuartzSubscriberService extends AbstractService implements SubscriberService, QuartzConstants {
    private final Collection<Scheduler> schedulers;
    private final SubscriberEndpoint endpoint;
    private final PlatformContext platformContext;

    private static final int STARTED = 0, STOPPED = 1;
    private volatile int state = STOPPED;

    public QuartzSubscriberService(SubscriberEndpoint endpoint, PlatformContext platformContext) {
        this.endpoint = endpoint;
        this.platformContext = platformContext;
        this.schedulers = new ArrayList<>();
    }

    @Override
    public void addRoute(SubscriberRoute subscriberRoute) {
        Set<String> topics = new HashSet<>();
        topics.addAll(subscriberRoute.topics());
        topics.addAll(subscriberRoute.queues());

        if (subscriberRoute.consumers().size() > 1) {
            throw new IllegalArgumentException("Multiple Consumers are not supported");
        }

        for (String topic : topics) {
            try {
                SubscriberRoute.ConsumerGroup consumerGroup = subscriberRoute.consumers().iterator().next();
                SimpleConsumer simpleConsumer = (SimpleConsumer) consumerGroup.getConsumer();
                if (simpleConsumer == null) {
                    simpleConsumer = platformContext.get(consumerGroup.getConsumerRef(), SimpleConsumer.class);
                }

                QuartzConf configuration = (QuartzConf) endpoint.getConfiguration();
                RDBMS rdbms = configuration.getRdbms();
                String dataSourceRef = rdbms.getID() + RDBMS.DS;
                DataSource dataSource = platformContext.get(dataSourceRef, DataSource.class);
                ConnectionProviderImpl connectionProvider = new ConnectionProviderImpl(dataSource);
                DBConnectionManager.getInstance().addConnectionProvider(dataSourceRef, connectionProvider);

                Properties quartzProperties = getQuartzProperties(topic, dataSourceRef, consumerGroup.getConcurrentConsumers(), configuration.getTablePrefix());
                Scheduler scheduler = new StdSchedulerFactory(quartzProperties).getScheduler();

                scheduler.setJobFactory(new QuartzJobFactory(simpleConsumer, subscriberRoute.serializer()));
                schedulers.add(scheduler);

                if (state == STARTED) {
                    scheduler.start();
                }

            } catch (SchedulerException e) {
                throw new RuntimeException("Unable to Create Quartz Scheduler", e);
            }
        }
    }

    @Override
    protected void doStart() throws Exception {
        if (state == STARTED) {
            return;
        }
        for (Scheduler scheduler : schedulers) {
            scheduler.start();
        }
        state = STARTED;
    }

    @Override
    protected void doShutdown() {
        if (state == STOPPED) {
            return;
        }
        if (!schedulers.isEmpty()) {
            for (Scheduler scheduler : schedulers) {
                try {
                    scheduler.shutdown(true);
                } catch (SchedulerException e) {
                    throw new RuntimeException("Unable to stop scheduler", e);
                }
            }
        }
        state = STOPPED;
    }

    private Properties getQuartzProperties(String topic, String dataSource, int concurrentConsumers, String tablePrefix) {
        Properties properties = new Properties();
        properties.put(INSTANCE_NAME, topic);
        properties.put(INSTANCE_ID, topic + "_CONSUMER");
        properties.put(THREAD_COUNT, String.valueOf(concurrentConsumers));
        properties.put(THREAD_POOL_CLASS, SimpleThreadPool.class.getCanonicalName());
        properties.put(JOB_STORE_CLASS, JobStoreTX.class.getCanonicalName());
        properties.put(TABLE_PREFIX, tablePrefix);
        properties.put(DRIVER_DELEGATE_CLASS, StdJDBCDelegate.class.getCanonicalName());
        properties.put(DATASOURCE, dataSource);
        return properties;
    }
}
