package com.whilter.pubsub;


/**
 * Created by deepakchauhan on 09/07/17.
 */
public interface BatchConsumer<T> extends Consumer<T> {

    void consume(Batch<T> batch);

}
