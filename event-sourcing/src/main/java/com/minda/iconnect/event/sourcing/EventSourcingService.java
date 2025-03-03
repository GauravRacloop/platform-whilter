package com.minda.iconnect.event.sourcing;

import com.whilter.core.Service;


/**
 * @author jaspreet on 15/01/19
 */
public interface EventSourcingService extends Service {

    void write(String key, String string);

    void read(String key, Function function);

    interface Function {
        void invoke(String msg);
    }
}
