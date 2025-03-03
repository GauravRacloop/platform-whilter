package com.minda.quartz.test;

import com.minda.iconnect.platform.AbstractSpringConfig;
import com.minda.iconnect.platform.PlatformConfig;
import com.minda.iconnect.platform.pubsub.ConsumerProxy;
import com.minda.iconnect.platform.pubsub.Producer;
import com.minda.iconnect.platform.pubsub.serializers.KryoSerializer;
import com.minda.iconnect.platform.rdbms.EnableRDBMS;
import com.minda.iconnect.platform.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.minda.iconnect.platform.pubsub.EnablePubSub;
import com.minda.iconnect.platform.pubsub.Pubsub;

/**
 * Created by mayank on 10/08/18 12:17 PM.
 */


@Configuration
@Import({PlatformConfig.class})
@EnableRDBMS(confId = "carot-db",
        persistenceUnit = "carot",
        packagesToScan = "com.minda.quartz.test")
@EnablePubSub({
        @Pubsub(confId = "quartz-test", producerBean = "producer1", consumerBean = "quartzConsumer1", serializerFactoryCls = KryoSerializer.class)
})
public class QuartzTest extends AbstractSpringConfig {

    public static void main(String[] args) throws Exception {
        Runner.main(new String[]{"--spring", "com.minda.quartz.test.QuartzTest"});
    }

    @Bean("quartzConsumer1")
    public QuartzConsumer quartzConsumer1() {
        return new QuartzConsumer("Consumer1");
    }

    @Autowired
    public void start(@Named("producer1") Producer producer1) {
        new Thread(() -> {
            AtomicInteger integer = new AtomicInteger();

            while (integer.incrementAndGet() < 102) {
                Producer.Message prod1 = new Producer.Message("Prod1" + new Date() + " ------------ " + integer.get());
                prod1.setDelay(1000L);
                producer1.produce(prod1);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.exit(0);
        }).start();
    }
}