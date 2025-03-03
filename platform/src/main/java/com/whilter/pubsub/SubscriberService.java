package com.whilter.pubsub;

import com.whilter.core.Service;

/**
 * Created by deepakchauhan on 16/07/17.
 */
public interface SubscriberService extends Service {

    void addRoute(SubscriberRoute subscriberRoute);

}
