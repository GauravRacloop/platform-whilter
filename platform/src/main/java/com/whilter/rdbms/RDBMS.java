package com.whilter.rdbms;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.whilter.conf.ConfArray;
import com.whilter.conf.internal.AbstractConfiguration;


@JsonRootName("rdbms")
@ConfArray(RDBMS[].class)
public class RDBMS extends AbstractConfiguration {

    public static final String TX = "TX";
    public static final String EMF = "EMF";
    public static final String DS = "DS";
    public static final String jOOQ = "jOOQ";
    public static final String jOOQDSL = "jOOQDSL";

    private String driverClassName;
    private String host;
    private int port;
    private String username;
    private String password;
    private boolean autoCommit;
    private int connectionTimeout;
    private int idleTimeout;
    private int minimumIdle;
    private int minPoolSize;
    private int maximumPoolSize;
    private String schema;
    private Database database;
    private String jdbcUrl;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public int getMinimumIdle() {
        return minimumIdle;
    }

    public void setMinimumIdle(int minimumIdle) {
        this.minimumIdle = minimumIdle;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUrl() {
        if (jdbcUrl != null) {
            return jdbcUrl;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(database == null ? Database.MYSQL.getConnectionProtocol() : database.getConnectionProtocol());

        stringBuilder.append(host)
                .append(":")
                .append(port <= 0 ? 3306 : port).append("/")
                .append(schema);

        return stringBuilder.toString();
    }

    public enum Database {
        H2("jdbc:h2://"),
        HSQLDB("jdbc:hsqldb://"),
        MYSQL("jdbc:mysql://"),
        SQL99(""),
        DEFAULT(""),
        CUBRID(""),
        DERBY(""),
        FIREBIRD(""),
        FIREBIRD_2_5(""),
        FIREBIRD_3_0(""),
        MARIADB(""),
        MYSQL_5_7(""),
        MYSQL_8_0(""),
        POSTGRES(""),
        POSTGRES_9_3(""),
        POSTGRES_9_4(""),
        POSTGRES_9_5(""),
        SQLITE(""),
        SQLSERVER("");

        private String connectionProtocol;

        Database(String connectionProtocol) {
            this.connectionProtocol = connectionProtocol;
        }

        public String getConnectionProtocol() {
            return connectionProtocol;
        }
    }

}
