package com.whilter.conf.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.whilter.conf.Configuration;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by deepakchauhan on 13/07/17.
 */
public class ConfigHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHolder.class);

    private static final String ENCODING = "UTF-8";

    private Object config;

    ConfigHolder(InputStream inputStream) throws Exception {
        init(inputStream);
    }

    private void init(InputStream stream) throws Exception {
        Class<?> wrapperCls = wrapRegistry();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);


        stream = IOUtils.toInputStream(resolveEnvVars(stream), "UTF-8");

        Yaml yaml = new Yaml();
        Map<?, ?> map = (Map<?, ?>) yaml.load(stream);
        config = mapper.readValue(mapper.writeValueAsString(map), wrapperCls);
    }

    private Class<?> wrapRegistry() throws Exception {
        Map<String, ConfigRegistryHolder.ConfRegistry> registryMap = ConfigRegistryHolder.getInstance().getRegistry();

        DynamicType.Builder<?> builder = new ByteBuddy().subclass(Object.class);

        for (ConfigRegistryHolder.ConfRegistry registry : registryMap.values()) {
            builder = builder.defineField(registry.getName(), registry.getType(), Visibility.PUBLIC);
        }
        return builder.make().load(DefaultConfigReader.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();
    }

    public <T extends Configuration> Collection<T> readValue(Class<T> type) throws Exception {
        boolean interfaze = type.isInterface();
        if (!interfaze) interfaze = Modifier.isAbstract(type.getModifiers());

        Collection<Class<T>> types = new ArrayList<>();
        if (!interfaze) {
            types.add(type);
        } else {
            types = getImplementingClasses(type);
        }
        Collection<T> confs = new ArrayList<>();
        for (Class<T> tClass : types) {
            Collection<T> ts = readValueInternal(tClass);
            if (ts != null) {
                confs.addAll(ts);
            }
        }
        return confs;
    }


    private <T extends Configuration> Collection<T> readValueInternal(Class<T> type) throws Exception {
        Map<String, ConfigRegistryHolder.ConfRegistry> registryMap = ConfigRegistryHolder.getInstance().getRegistry();
        ConfigRegistryHolder.ConfRegistry registry = registryMap.get(type.getName());
        if (registry == null) {
            throw new InvalidClassException("No configuration found for class: " + type + " Make sure config is registered in registry file.");
        }
        Collection<T> list = new ArrayList<>();
        if (registry.getConfArray() != null) {
            T[] ll = (T[]) config.getClass().getDeclaredField(registry.name).get(config);
            if (ll != null) {
                for (T t : ll) {
                    if (t != null) list.add(t);
                }
            }
        } else {
            T obj = (T) config.getClass().getDeclaredField(registry.name).get(config);
            if (obj != null) {
                list.add(obj);
            }
        }
        return list;
    }

    private <T> Collection<Class<T>> getImplementingClasses(Class<T> type) {
        Collection<Class<T>> classes = new ArrayList<>();
        Map<String, ConfigRegistryHolder.ConfRegistry> registryMap = ConfigRegistryHolder.getInstance().getRegistry();
        for (ConfigRegistryHolder.ConfRegistry registry : registryMap.values()) {
            if (type.isAssignableFrom(registry.getCls())) {
                classes.add((Class<T>) registry.cls);
            }
        }
        return classes;
    }

    private static String resolveEnvVars(InputStream stream) throws IOException {
        if (null == stream) {
            return "";
        }

        StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer, ENCODING);
        IOUtils.closeQuietly(stream);

        String input = writer.toString();
        writer.close();

        Map<String, String> envMap = System.getenv();
        Pattern p = Pattern.compile("\\$\\{([A-Za-z0-9-_]+)\\}");
        Matcher matcher = p.matcher(input);
        while (matcher.find()) {
            String envValue = envMap.get(matcher.group(1).toUpperCase());
            if (envValue == null) {
                envValue = "";
            } else {
                envValue = envValue.replace("\\", "\\\\");
            }
            Pattern subexpr = Pattern.compile(Pattern.quote(matcher.group(0)));
            input = subexpr.matcher(input).replaceAll(envValue);
        }
        return input;
    }
}
