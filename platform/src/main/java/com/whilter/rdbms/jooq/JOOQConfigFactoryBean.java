package com.whilter.rdbms.jooq;


import com.whilter.rdbms.RDBMS;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by deepakchauhan on 04/08/17.
 */
public class JOOQConfigFactoryBean implements FactoryBean<Configuration> {

    private RDBMS rdbms;

    private DataSource dataSource;
    private PlatformTransactionManager transactionManager;

    public JOOQConfigFactoryBean(RDBMS rdbms) {
        this.rdbms = rdbms;
    }

    @Override
    public Configuration getObject() throws Exception {
        return jooqConfig();
    }

    @Override
    public Class<?> getObjectType() {
        return Configuration.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    private org.jooq.Configuration jooqConfig() {
        org.jooq.Configuration configuration = new DefaultConfiguration()
                .derive(new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource)))
                .derive(new SpringTransactionProvider(transactionManager))
                .derive(new DefaultExecuteListenerProvider(new ExceptionTranslator()))
                .derive(SQLDialect.valueOf(rdbms.getDatabase().name()));

        Settings settings = new Settings();
        settings.setUpdatablePrimaryKeys(false);
        settings.withExecuteWithOptimisticLocking(false);
        settings.setStatementType(StatementType.PREPARED_STATEMENT);
        ((DefaultConfiguration) configuration).setSettings(settings);
        return configuration;
    }
}
