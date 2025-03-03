package com.whilter.pubsub;

import com.whilter.core.Service;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by deepakchauhan on 09/07/17.
 */
public interface C2DProducer extends Service {

    void produce(String destination, Message message);

    void produce(String destination, Collection<Message> messages);

    void produce(String destination, Stream<Message> messages);

    void produce(String destination, Message message, AsyncCallback callback);

    void produce(String destination, Collection<Message> messages, AsyncCallback callback);

    void produce(String destination, Stream<Message> messages, AsyncCallback callback);

    void stop();

    interface AsyncCallback {
        void onCompletion(Metadata metadata, Exception e);
    }

    class Message {
        String key;
        int delay;
        Object data;

        public Message(Object data) {
            this.data = data;
        }

        public Message(String key, Object data) {
            this.key = key;
            this.data = data;
        }

        public int getDelay() {
            return delay;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }

        public String getKey() {
            return key;
        }

        public Object getData() {
            return data;
        }
    }
}
