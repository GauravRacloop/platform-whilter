package com.whilter.conf.internal;

import com.whilter.conf.ConfArray;
import com.whilter.conf.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by deepakchauhan on 14/07/17.
 */
final class ConfigRegistryHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHolder.class);
    private static ConfigRegistryHolder HOLDER;

    static {
        try {
            HOLDER = new ConfigRegistryHolder();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, ConfRegistry> registry = new HashMap<>();

    private ConfigRegistryHolder() throws Exception {
        wrapRegistry();
    }

    public static ConfigRegistryHolder getInstance() {
        return HOLDER;
    }

    public Map<String, ConfRegistry> getRegistry() {
        return registry;
    }

    private void wrapRegistry() throws Exception {
        for (Map.Entry<Object, Object> entry : loadConfigRegistry().entrySet()) {
            String configName = entry.getKey().toString();
            if (entry.getValue() == null) {
                LOGGER.warn("Config Registry: " + " class is not available for " + configName);
                continue;
            }
            try {
                Class<? extends ConfigReader> cls = (Class<? extends ConfigReader>) Class.forName(entry.getValue().toString());
                ConfArray confArray = cls.getAnnotation(ConfArray.class);
                registry.put(cls.getName(), new ConfRegistry(configName, cls, confArray));
            } catch (Exception e) {
                LOGGER.warn("Config Registry: " + " class not found in classpath " + configName + ". Class Name: " + entry.getValue().toString()
                        + " It may lead to exception, if used in application");
            }
        }
    }

    private Map<Object, Object> loadConfigRegistry() throws Exception {
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] providerMapping = patternResolver.getResources("classpath*:META-INF/registry/conf");
        Properties properties = new Properties();
        for (Resource mapping : providerMapping) {
            properties.load(mapping.getInputStream());
        }
        return properties;
    }

    public static class ConfRegistry {
        String name;
        Class<?> cls;
        ConfArray confArray;

        public ConfRegistry(String name, Class<?> cls, ConfArray confArray) {
            this.name = name;
            this.cls = cls;
            this.confArray = confArray;
        }

        public String getName() {
            return name;
        }

        public Class<?> getCls() {
            return cls;
        }

        public ConfArray getConfArray() {
            return confArray;
        }

        public Type getType() {
            if (confArray == null) {
                return cls;
            }
            return confArray.value();
        }
    }
}
