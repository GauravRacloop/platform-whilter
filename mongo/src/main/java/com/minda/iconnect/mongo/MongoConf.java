package com.minda.iconnect.mongo;

import com.minda.iconnect.platform.conf.ConfArray;
import com.minda.iconnect.platform.conf.Server;
import com.minda.iconnect.platform.conf.internal.AbstractConfiguration;

import java.util.Map;

/**
 * Created by deepakchauhan on 27/08/17.
 */
@ConfArray(MongoConf[].class)
public class MongoConf extends AbstractConfiguration {

    private Server[] servers;
    private String replicaSet;
    private String db;
    private int maxPoolSize;
    private String username;
    private String password;
    private String authDb;

    public Server[] getServers() {
        return servers;
    }

    public void setServers(Server[] servers) {
        this.servers = servers;
    }

    public String getReplicaSet() {
        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
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

    public String getAuthDb() {
        return authDb;
    }

    public void setAuthDb(String authDb) {
        this.authDb = authDb;
    }

    public String toConnectionString(String database, String collection) {
        StringBuilder stringBuilder = new StringBuilder("mongodb://");

        if (username != null && !username.trim().isEmpty()) {
            stringBuilder.append(username + ":" + password + "@");
        }

        int size = this.servers.length;
        int i = 0;
        for (Server server : this.servers) {
            stringBuilder.append(server.getHost() + ":" + server.getPort());
            if (i == size - 1)
                stringBuilder.append("/");
            if (i < size - 1)
                stringBuilder.append(",");
            i++;
        }

        if (database != null) {
            stringBuilder.append(database);
        }

        if (collection != null) {
            stringBuilder.append("." + collection);
        }

        /*if (null != replicaSet && !replicaSet.isEmpty()) {
            stringBuilder.append(separator + "replicaSet=" + this.getReplicaSet());
        }

        if (this.getMaxPoolSize() > 0) {
            stringBuilder.append(separator + "maxPoolSize=" + this.getMaxPoolSize());
        }

        if (getProperties() != null) {
            for (Map.Entry<String, Object> entry : getProperties().entrySet()) {
                stringBuilder.append(separator + entry.getKey() + "=" + entry.getValue());
            }
        }*/

        return stringBuilder.toString();
    }
}
