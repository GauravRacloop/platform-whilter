package com.whilter.rdbms;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.FactoryBean;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * Created by deepakchauhan on 23/07/17.
 */
public class DataSourceFactoryBean implements FactoryBean<DataSource> {

    private RDBMS rdbms;

    public DataSourceFactoryBean(RDBMS rdbms) {
        this.rdbms = rdbms;
    }

    @Override
    public DataSource getObject() throws Exception {
        HikariConfig hikariConfig = new HikariConfig();
        if (rdbms.getProperties() != null && rdbms.getProperties().size() > 0) {
            Properties properties = new Properties();
            for (Map.Entry<String, Object> entry : rdbms.getProperties().entrySet()) {
                properties.put(entry.getKey(), entry.getValue());
            }
            hikariConfig = new HikariConfig(properties);
        }

        hikariConfig.setDriverClassName(rdbms.getDriverClassName());
        hikariConfig.setJdbcUrl(rdbms.getJdbcUrl());
        hikariConfig.setUsername(rdbms.getUsername());
        hikariConfig.setPassword(rdbms.getPassword());
        hikariConfig.setAutoCommit(rdbms.isAutoCommit());
        hikariConfig.setConnectionTimeout(rdbms.getConnectionTimeout());
        hikariConfig.setIdleTimeout(rdbms.getIdleTimeout());
        hikariConfig.setMinimumIdle(rdbms.getMinimumIdle());
        hikariConfig.setMaximumPoolSize(rdbms.getMaximumPoolSize());

        return new HikariDataSource(hikariConfig);
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
