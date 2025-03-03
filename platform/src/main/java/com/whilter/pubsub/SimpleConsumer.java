package com.whilter.pubsub;


/**
 * Created by deepakchauhan on 09/07/17.
 */
public interface SimpleConsumer<T> extends Consumer<T> {

    void consume(T message);

}
