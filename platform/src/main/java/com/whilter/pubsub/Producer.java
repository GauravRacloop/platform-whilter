package com.whilter.pubsub;

import com.whilter.core.Service;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by deepakchauhan on 09/07/17.
 */
public interface Producer extends Service {

    void produce(Message message);

    void produce(Collection<Message> messages);

    void produce(Stream<Message> messages);

    void produce(Message message, AsyncCallback callback);

    void produce(Collection<Message> messages, AsyncCallback callback);

    void produce(Stream<Message> messages, AsyncCallback callback);

    void stop();

    interface AsyncCallback {
        void onCompletion(Metadata metadata, Exception e);
    }

    class Message {
        String key;
        long delay;
        Object data;

        public Message(Object data) {
            this.data = data;
        }

        public Message(String key, Object data) {
            this.key = key;
            this.data = data;
        }

        public long getDelay() {
            return delay;
        }

        public void setDelay(long delay) {
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
