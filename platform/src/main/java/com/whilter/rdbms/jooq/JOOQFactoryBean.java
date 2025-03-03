package com.whilter.rdbms.jooq;

import org.jooq.Configuration;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by deepakchauhan on 04/08/17.
 */
public class JOOQFactoryBean implements FactoryBean<JOOQ> {

    private Configuration configuration;

    public JOOQFactoryBean(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public JOOQ getObject() {
        return new JOOQ(configuration);
    }

    @Override
    public Class<?> getObjectType() {
        return JOOQ.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
