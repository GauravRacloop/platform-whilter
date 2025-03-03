package com.minda.iconnect.jaxrs.jersey.server;

import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.platform.jaxrs.JaxRSServer;
import org.glassfish.grizzly.http.server.HttpServer;

import java.io.IOException;

/**
 * Created by deepakchauhan on 14/07/17.
 */
public class JerseyServer extends AbstractService implements JaxRSServer {

    HttpServer httpServer;

    public JerseyServer(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    @Override
    protected void doStart() throws IOException {
        if (httpServer != null) httpServer.start();
    }

    @Override
    protected void doShutdown() {
        if (httpServer != null) httpServer.shutdown();
    }
}
