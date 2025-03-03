package com.whilter.pubsub;

import java.util.stream.Stream;

/**
 * Created by deepakchauhan on 13/08/17.
 */
public interface StreamingConsumer<T> extends Consumer<T> {

    void consume(Stream<T> stream);

}
