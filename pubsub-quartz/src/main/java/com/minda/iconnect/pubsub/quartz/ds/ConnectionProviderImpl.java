package com.minda.iconnect.pubsub.quartz.ds;

import org.quartz.utils.ConnectionProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by mayank on 13/08/18 11:27 AM.
 */
public class ConnectionProviderImpl implements ConnectionProvider {
    private final DataSource dataSource;

    public ConnectionProviderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void initialize() {

    }
}
