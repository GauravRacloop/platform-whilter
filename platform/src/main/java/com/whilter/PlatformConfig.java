package com.whilter;

import com.whilter.conf.ConfigReader;
import com.whilter.core.ComponentResolver;
import com.whilter.core.PlatformContext;
import com.whilter.core.internal.DefaultComponentResolver;
import com.whilter.core.internal.SpringPlatformContext;
import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import javax.inject.Named;

/**
 * Created by deepakchauhan on 20/05/17.
 */
@ImportResource({"classpath:META-INF/spring/platform.xml"})
public class PlatformConfig  {

    @Bean(name = "platformContext")
    public PlatformContext platformContext() {
        return new SpringPlatformContext();
    }

    @Bean(name = "configReader")
    public ConfigReader config() {
        return ConfigReader.get();
    }

    @Bean(name = "platformCamelContext")
    public CamelContext camelContext(ApplicationContext applicationContext) {
        CamelContext camelContext = new SpringCamelContext(applicationContext);
        camelContext.disableJMX();
        return camelContext;
    }

    @Bean(name = "componentResolver", initMethod = "start", destroyMethod = "shutdown")
    public ComponentResolver componentResolver(PlatformContext context,
                                               @Named("platformCamelContext") CamelContext camelContext) {
        return new DefaultComponentResolver(context, camelContext);
    }
}
