package com.whilter.pubsub.quartz.publisher;

import com.whilter.core.PlatformContext;
import com.whilter.core.internal.AbstractService;
import com.whilter.pubsub.Producer;
import com.whilter.pubsub.ProducerEndpoint;
import com.whilter.pubsub.ProducerRoute;
import com.whilter.pubsub.Serializer;
import com.whilter.rdbms.RDBMS;
import com.whilter.pubsub.quartz.QuartzConf;
import com.whilter.pubsub.quartz.constants.QuartzConstants;
import com.whilter.pubsub.quartz.ds.ConnectionProviderImpl;
import com.whilter.pubsub.quartz.job.SimpleJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.jdbcjobstore.JobStoreTX;
import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.utils.DBConnectionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Stream;


public class QuartzProducerService extends AbstractService implements Producer, QuartzConstants {

    private final Collection<Scheduler> schedulers;
    private final QuartzConf quartzConf;
    private final PlatformContext context;
    private final ProducerEndpoint endpoint;
    private final Serializer serializer;

    public QuartzProducerService(ProducerEndpoint endpoint, PlatformContext platformContext) {
        this.serializer = endpoint.getProducerRoute().serializer();
        if (endpoint.getConfiguration() == null || !(endpoint.getConfiguration() instanceof QuartzConf)) {
            throw new IllegalArgumentException("Quartz configuration not found in pipeline");
        }
        this.endpoint = endpoint;
        this.quartzConf = (QuartzConf) endpoint.getConfiguration();
        this.context = platformContext;
        this.schedulers = new ArrayList<>();
    }

    @Override
    protected void doStart() {
        try {
            RDBMS rdbms = quartzConf.getRdbms();
            String dataSourceRef = rdbms.getID() + RDBMS.DS;
            DataSource dataSource = context.get(dataSourceRef, DataSource.class);
            ConnectionProviderImpl connectionProvider = new ConnectionProviderImpl(dataSource);
            DBConnectionManager.getInstance().addConnectionProvider(dataSourceRef, connectionProvider);
            for (ProducerRoute.ProducerGroup producerGroup : endpoint.getProducerRoute().producers()) {
                Properties quartzProperties = getQuartzProperties(producerGroup.getDestination(), dataSourceRef, quartzConf.getTablePrefix());
                Scheduler scheduler = new StdSchedulerFactory(quartzProperties).getScheduler();
                schedulers.add(scheduler);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException("Unable to Create Quartz Scheduler", e);
        }
    }

    @Override
    protected void doShutdown() {
        //DO Nothing
    }

    @Override
    public void produce(Message message) {
        produce(message, null);

    }

    @Override
    public void produce(Collection<Message> messages) {
        messages.forEach(this::produce);
    }

    @Override
    public void produce(Stream<Message> messages) {
        messages.forEach(this::produce);
    }

    @Override
    public void produce(Message message, AsyncCallback callback) {
        for (Scheduler scheduler : schedulers) {
            produceInternal(message, callback, scheduler);
        }
    }

    private void produceInternal(Message message, AsyncCallback callback, Scheduler scheduler) {
        JobDataMap data = new JobDataMap();
        data.put(DATA_KEY, serializer.serialize(message.getData()));

        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).usingJobData(data).build();

        TriggerBuilder<Trigger> triggerTriggerBuilder = TriggerBuilder.newTrigger()
                .forJob(jobDetail);

        TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow());

        if (message.getDelay() > 0L) {
            triggerTriggerBuilder = triggerTriggerBuilder.startAt(new Date(System.currentTimeMillis() + message.getDelay()));
        }

        ProducerRoute.MisFireStrategy misFireStrategy = ProducerRoute.MisFireStrategy.FIRE_NOW;

        if (endpoint.getProducerRoute().misfireStrategy() != null) {
            misFireStrategy = endpoint.getProducerRoute().misfireStrategy();
        }

        switch (misFireStrategy) {
            case IGNORE:
                triggerTriggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionIgnoreMisfires());
                break;
            case FIRE_NOW:
                triggerTriggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow());
                break;
            default:
                break;
        }
        Trigger trigger = triggerTriggerBuilder.build();

        Exception exception = null;
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            exception = e;
            throw new RuntimeException("Unable to Produce Message to Quartz", e);
        } finally {
            if (callback != null) {
                callback.onCompletion(null, exception);
            }
        }

    }

    @Override
    public void produce(Collection<Message> messages, AsyncCallback callback) {
        messages.forEach(message -> produce(message, callback));
    }

    @Override
    public void produce(Stream<Message> messages, AsyncCallback callback) {
        messages.forEach(message -> produce(message, callback));
    }

    @Override
    public void stop() {
        //DO Nothing
    }

    private Properties getQuartzProperties(String topic, String datasource, String tablePrefix) {
        Properties properties = new Properties();
        properties.put(INSTANCE_NAME, topic);
        properties.put(INSTANCE_ID, topic + "_PRODUCER");
        properties.put(THREAD_COUNT, "1");
        properties.put(THREAD_POOL_CLASS, SimpleThreadPool.class.getCanonicalName());
        properties.put(JOB_STORE_CLASS, JobStoreTX.class.getCanonicalName());
        properties.put(TABLE_PREFIX, tablePrefix);
        properties.put(DRIVER_DELEGATE_CLASS, StdJDBCDelegate.class.getCanonicalName());
        properties.put(DATASOURCE, datasource);
        return properties;
    }

}
