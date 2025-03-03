package com.minda.iconnect.pubsub.mail;

import com.minda.iconnect.platform.AbstractSpringConfig;
import com.minda.iconnect.platform.PlatformConfig;
import com.minda.iconnect.platform.pubsub.ConsumerProxy;
import com.minda.iconnect.platform.pubsub.EnablePubSub;
import com.minda.iconnect.platform.pubsub.Pubsub;
import com.minda.iconnect.platform.pubsub.SimpleConsumer;
import com.minda.iconnect.platform.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.inject.Named;

/**
 * Created by mayank on 10/01/19 2:47 PM.
 */
@Import({PlatformConfig.class})
@EnablePubSub({
        @Pubsub(confId = "mail-reader", consumerBean = "mailConsumer")
})
public class PubsubMailTest extends AbstractSpringConfig {

    @Bean("mailConsumer")
    public MailConsumer mailConsumer() {
        return new MailConsumer();
    }

    @Autowired
    public void start(@Named("mailConsumer_proxy")ConsumerProxy consumerProxy) {
        consumerProxy.toString();
    }

    public static void main(String[] args) throws Exception {
        Runner.main(new String[]{"--spring", "com.minda.iconnect.pubsub.mail.PubsubMailTest"});
    }

    public class MailConsumer implements SimpleConsumer<EMail> {

        @Override
        public void consume(EMail message) {
            System.out.println(message);
        }
    }
}
