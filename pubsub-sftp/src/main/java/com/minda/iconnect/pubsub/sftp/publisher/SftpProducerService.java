package com.minda.iconnect.pubsub.sftp.publisher;

import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.platform.pubsub.Producer;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author thanos on 21/05/19
 */
public class SftpProducerService extends AbstractService implements Producer {
    @Override
    protected void doStart() {
        //do nothing
    }

    @Override
    protected void doShutdown() {
        //do nothing
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
        throw new UnsupportedOperationException("Currently SFTP Copy Operations is not Supported");
    }

    @Override
    public void produce(Collection<Message> messages, AsyncCallback callback) {
        messages.forEach(val -> produce(val, callback));
    }

    @Override
    public void produce(Stream<Message> messages, AsyncCallback callback) {
        messages.forEach(val -> produce(val, callback));
    }

    @Override
    public void stop() {
        //do nothing
    }
}
