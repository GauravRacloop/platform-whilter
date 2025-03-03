package com.minda.iconnect.elasticsearch;

import com.minda.iconnect.platform.conf.Server;
import com.whilter.core.internal.AbstractService;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by deepakchauhan on 23/07/17.
 */
public class Elastic extends AbstractService {

    private ElasticEndpoint endpoint;

    private TransportClient transportClient;

    public Elastic(ElasticEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    protected void doStart() throws IOException {
        Settings settings = Settings.builder().put("cluster.name", endpoint.getElasticConf().getCluster()).build();
        transportClient = new PreBuiltTransportClient(settings);
        for (Server server : endpoint.getElasticConf().getServers()) {
            transportClient = transportClient.
                    addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(server.getHost()), server.getPort()));
        }
    }

    @Override
    protected void doShutdown() {
        transportClient.close();
    }

    public TransportClient getTransportClient() {
        return transportClient;
    }
}
