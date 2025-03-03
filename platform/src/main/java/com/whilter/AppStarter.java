package com.whilter;

import com.whilter.conf.Active;
import com.whilter.conf.ConfigReader;

import com.whilter.runner.Runner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.*;

public class AppStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) throws Exception {
        start(args, true);
    }

    public static void start(String[] args, boolean join) throws Exception {
        ConfigReader configReader = ConfigReader.get();
        Active active = configReader.readSingleAny(Active.class);
        Map<String, Class<?>> appRegistry = loadAppConfig();
        ArrayList<String> params = new ArrayList<>();

        if (args.length > 0) {
            for (String arg : args) {
                if (arg != null && !arg.trim().isEmpty()) {
                    String app = arg;
                    addParams(appRegistry, params, app);
                }
            }
        } else {
            for (String app : active.getValues()) {
                addParams(appRegistry, params, app);
            }
        }
        Runner.run(params.toArray(new String[params.size()]), join);
    }

    private static void addParams(Map<String, Class<?>> appRegistry, ArrayList<String> params, String app) {
        Class<?> appCls = appRegistry.get(app);
        if (app != null && appCls != null) {
            params.add("--spring");
            params.add(appCls.getCanonicalName());
        }
    }

    private static Map<String, Class<?>> loadAppConfig() throws Exception {
        Map<String, Class<?>> configMap = new LinkedHashMap<>();
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] providerMapping = patternResolver.getResources("classpath*:META-INF/registry/app");
        Properties properties = new Properties();
        for (Resource mapping : providerMapping) {
            properties.load(mapping.getInputStream());
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String name = entry.getKey().toString();
                try {
                    Class<?> cls = Class.forName(entry.getValue().toString());
                    configMap.put(name, cls);
                } catch (Exception e) {
                    LOGGER.error("App Registry: " + " class not found in classpath for " +
                            name + ". Class Name: " + entry.getValue().toString());
                    throw e;
                }
            }
        }
        return configMap;
    }
}
