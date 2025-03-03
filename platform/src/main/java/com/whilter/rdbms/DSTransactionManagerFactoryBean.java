package com.whilter.rdbms;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by deepakchauhan on 23/07/17.
 */
public class DSTransactionManagerFactoryBean implements FactoryBean<PlatformTransactionManager> {

    private DataSource dataSource;

    public DSTransactionManagerFactoryBean(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public PlatformTransactionManager getObject() throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

    @Override
    public Class<?> getObjectType() {
        return DataSourceTransactionManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
