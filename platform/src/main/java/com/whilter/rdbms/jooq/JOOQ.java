package com.whilter.rdbms.jooq;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DefaultDSLContext;

public class JOOQ {

    private final ThreadLocal<DSLContext> configurationThreadLocal;

    JOOQ(Configuration configuration) {
        this.configurationThreadLocal = ThreadLocal.withInitial(() -> new DefaultDSLContext(configuration));
    }

    public DSLContext createContext() {
        DSLContext dslContext = configurationThreadLocal.get();
        return dslContext;
    }
}
