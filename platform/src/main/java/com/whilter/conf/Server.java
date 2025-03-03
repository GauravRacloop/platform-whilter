package com.whilter.conf;

import com.fasterxml.jackson.annotation.JsonValue;
import com.whilter.conf.internal.AbstractConfiguration;

/**
 * Created by deepakchauhan on 09/07/17.
 */
@ConfArray(Server[].class)
public class Server extends AbstractConfiguration {

    protected String host;
    protected int port;
    protected int connectionTimeout;
    protected int readTimeout;

    private String url;

    public Server() {}

    public Server(String url) {
        setUrl(url);
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

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setUrl(String url) {
        this.url = url;
        String[] hp = this.url.split(":");
        this.host = hp[0];
        this.port = Integer.valueOf(hp[1]);
    }

    @JsonValue
    public String getUrl() {
        if (url == null) {
            url = host + ':' + port;
        }
        return url;
    }
}
