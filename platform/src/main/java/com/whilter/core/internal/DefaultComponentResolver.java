package com.whilter.core.internal;

import com.whilter.core.Component;
import com.whilter.core.ComponentNotFoundException;
import com.whilter.core.ComponentResolver;
import com.whilter.core.PlatformContext;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by deepakchauhan on 15/07/17.
 */
public class DefaultComponentResolver implements ComponentResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultComponentResolver.class);

    private Map<String, Class<?>> registryMap = new HashMap<>();
    private Map<String, Component> componentMap = new HashMap<>();

    private PlatformContext platformContext;

    private CamelContext camelContext;

    public DefaultComponentResolver(PlatformContext context) {
        this.platformContext = context;
    }

    public DefaultComponentResolver(PlatformContext context, CamelContext camelContext) {
        this.platformContext = context;
        this.camelContext = camelContext;
    }


    @Override
    public <T extends Component> T resolve(String name, Class<T> type) {
        Class<?> cmpCls = registryMap.get(name);
        if (name != null && cmpCls == null) {
            throw new ComponentNotFoundException("No class found for the component: " + name);
        }

        if (name == null) {
            for (Class<?> aClass : registryMap.values()) {
                if (type.isAssignableFrom(aClass)) {
                    cmpCls = aClass;
                    break;
                }
            }
        }

        if (cmpCls == null) {
            throw new ComponentNotFoundException("No class found for the component Type: " + type);
        }

        try {
            T instance;
            if (Component.prototype(cmpCls)) {
                instance = createInstance(cmpCls);
                componentMap.put(UUID.fromString(cmpCls.getName()).toString(), instance);
            } else {
                instance = (T) componentMap.get(cmpCls.getName());
                if (instance == null) {
                    instance = createInstance(cmpCls);
                    componentMap.put(cmpCls.getName(), instance);
                }
            }
            return instance;
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            LOGGER.error("Error initializing the component with name: " + name + " and class: " + type.getCanonicalName(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private <T extends Component> T createInstance(Class<?> cmpCls) throws InstantiationException, IllegalAccessException, IOException {
        T instance = (T) cmpCls.newInstance();
        if (instance instanceof AbstractComponent) {
            ((AbstractComponent) instance).setContext(platformContext);
        }

        if (instance instanceof AbstractCamelComponent) {
            if (camelContext == null) {
                initCamelContext();
            }
            ((AbstractCamelComponent) instance).setCamelContext(camelContext);
        }
        instance.init();
        return instance;
    }

    private synchronized void initCamelContext() {
        if (camelContext == null) {
            if (platformContext instanceof SpringPlatformContext) {
                camelContext = new SpringCamelContext(((SpringPlatformContext) platformContext).getContext());
            } else {
                camelContext = new DefaultCamelContext();
            }
            camelContext.disableJMX();

            try {
                camelContext.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public <T extends Component> T resolveAny(Class<T> type) {
        return resolve(null, type);
    }

    @Override
    public void start() throws IOException {
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] providerMapping = patternResolver.getResources("classpath*:META-INF/registry/component");
        Properties properties = new Properties();
        for (Resource mapping : providerMapping) {
            properties.load(mapping.getInputStream());
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String cmpName = entry.getKey().toString();
                try {
                    Class<?> cls = Class.forName(entry.getValue().toString());
                    registryMap.put(cmpName, cls);
                } catch (Exception e) {
                    LOGGER.warn("Component Registry: " + " class not found in classpath for " + cmpName + ". Class Name: " + entry.getValue().toString()
                            + " It may lead to exception, if used in application");
                }

            }
        }
    }

    @Override
    public void shutdown() {
        for (Component component : componentMap.values()) {
            component.destroy();
        }
        componentMap.clear();
    }

    @Override
    public boolean autoStart() {
        return true;
    }
}
