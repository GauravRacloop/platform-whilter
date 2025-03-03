package com.whilter.pubsub;

/**
 * Created by deepakchauhan on 09/07/17.
 */
public interface ExceptionHandler {

    void handle(Exception e, Metadata metadata);

}
