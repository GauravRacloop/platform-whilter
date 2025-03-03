package test.com.minda.iconnect.pubsub.kafka;

import com.minda.iconnect.platform.AbstractSpringConfig;
import com.minda.iconnect.platform.PlatformConfig;
import com.minda.iconnect.platform.pubsub.*;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;

/**
 * Created by deepakchauhan on 08/11/17.
 */
@Configuration
@Import(PlatformConfig.class)
@EnablePubSub({
        @Pubsub(confId = "eventhub", consumerBean = "test")
})
public class KafkaTestCase extends AbstractSpringConfig {

    @Bean("test")
    public TestConsumer testConsumer() {
        return new TestConsumer();
    }

    @Autowired
    public void doStart(@Named("eventhub_proxy") ConsumerProxy proxy) {
        //DO NOTHING.
    }

    public static void main(String[] args) {

    }

    static class TestConsumer implements SimpleConsumer<String> {

        @Override
        public void consume(String message) {
            System.out.println("message:: " + message);
        }
    }

    static class AsItIsSerializer implements Serializer<String> {

        @Override
        public byte[] serialize(String object) {
            return object.getBytes();
        }

        @Override
        public String deserialize(byte[] channel) {
            return new String(channel);
        }
    }
}
