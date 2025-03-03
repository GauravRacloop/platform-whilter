/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2016 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.jersey.grizzly2.httpserver;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.grizzly.utils.Charsets;
import org.glassfish.jersey.process.JerseyProcessingUncaughtExceptionHandler;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public final class CarotGrizzlyHttpServerFactory {

    private HttpServer server;

    public CarotGrizzlyHttpServerFactory(HttpServer server) {
        this.server = server;
    }

    public HttpServer createHttpServer(final URI uri, final ResourceConfig resourceConfig, SSLEngineConfigurator sslEngineConfigurator) {

        GrizzlyHttpContainer handler = new GrizzlyHttpContainer(resourceConfig);

        if (server == null) {
            final String host = (uri.getHost() == null) ? NetworkListener.DEFAULT_NETWORK_HOST : uri.getHost();
            final int port = uri.getPort();

            final NetworkListener listener = new NetworkListener("iconnect", host, port);

            listener.getTransport().getWorkerThreadPoolConfig().setThreadFactory(new ThreadFactoryBuilder()
                    .setNameFormat("iconnect-http-server-%d")
                    .setUncaughtExceptionHandler(new JerseyProcessingUncaughtExceptionHandler())
                    .build());

            if (sslEngineConfigurator != null) {
                listener.setSecure(true);
                listener.setSSLEngineConfig(sslEngineConfigurator);
            }


            server = new HttpServer();
            server.addListener(listener);
        }

        // Map the path to the processor.
        final ServerConfiguration config = server.getServerConfiguration();
        if (handler != null) {
            final String path = uri.getPath().replaceAll("/{2,}", "/");

            final String contextPath = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
            config.addHttpHandler(handler, HttpHandlerRegistration.builder().contextPath(contextPath).build());
        }

        config.setPassTraceRequest(true);
        config.setDefaultQueryEncoding(Charsets.UTF8_CHARSET);
        return server;
    }
}
