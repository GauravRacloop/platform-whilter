package com.whilter.core;

import java.io.IOException;

/**
 * Created by deepakchauhan on 27/06/17.
 */
public interface Component<E extends Endpoint, S extends Service> {

    static boolean prototype(Class<?> cls) {
        com.whilter.core.Prototype prototype = cls.getAnnotation(Prototype.class);
        return prototype != null;
    }

    void init() throws IOException;

    void destroy();

    S get(E endpoint);
}
