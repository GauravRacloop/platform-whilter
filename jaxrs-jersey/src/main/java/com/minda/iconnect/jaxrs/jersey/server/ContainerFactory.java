package com.minda.iconnect.jaxrs.jersey.server;

import com.minda.iconnect.platform.jaxrs.JaxRSEndpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.ServletRegistration;

public class ContainerFactory {

    public void configure(JaxRSEndpoint endpoint, ResourceConfig resourceConfig) {
        ServletRegistration.Dynamic servletRegistration = AppInitializer.ctx.addServlet(endpoint.getService().getID(),
                new ServletContainer(resourceConfig));
        servletRegistration.addMapping(endpoint.getService().getHosts()[0].getContextPath());
        servletRegistration.setLoadOnStartup(1);
    }

}
