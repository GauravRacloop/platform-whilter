package com.minda.quartz.test;

import com.minda.iconnect.platform.pubsub.SimpleConsumer;

/**
 * Created by mayank on 13/08/18 4:43 PM.
 */
public class QuartzConsumer implements SimpleConsumer<String> {

    private final String val;

    public QuartzConsumer(String val) {
        this.val = val;
    }

    @Override
    public void consume(String message) {
        System.out.println("QuartzConsumer::" + val + " :: " + message + " Thread::" + Thread.currentThread().getId());
    }
}
