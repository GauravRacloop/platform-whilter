package com.whilter.conf.internal;

import com.whilter.conf.ConfigReader;
import com.whilter.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;

/**
 * Created by deepakchauhan on 13/07/17.
 */
@SuppressWarnings("Duplicates")
public class DefaultConfigReader implements ConfigReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfigReader.class);

    private static final String DEFAULT_CONFIG = "app-config.yaml";
    private static final String DEFAULT_HOME_PROPERTY = "APP_HOME";
    private static final String USER_HOME_PROP = "user.home";
    private static String DEFAULT_PROFILE = "";
    private static final DefaultConfigReader CONFIG_READER = new DefaultConfigReader();
    private static String CONFIG_FILE = DEFAULT_CONFIG;
    private static String HOME_DIR_PROPERTY = DEFAULT_HOME_PROPERTY;
    private static String PROFILE = DEFAULT_PROFILE;

    static {
        String configFile = System.getProperty("config.name");
        if (configFile != null && !configFile.isEmpty()) {
            CONFIG_FILE = configFile;
        }

        String homeDir = System.getProperty("home.dir");
        if (homeDir != null && !homeDir.isEmpty()) {
            HOME_DIR_PROPERTY = homeDir;
        }

        String profile = System.getProperty("profile");
        if (profile != null && !profile.isEmpty()) {
            PROFILE = profile;
        }

        if (!PROFILE.isEmpty()) {
            CONFIG_FILE = CONFIG_FILE + '.' + profile;
        }
    }

    private ConfigHolder configHolder;

    private DefaultConfigReader() {
    }

    public static DefaultConfigReader getInstance() {
        return CONFIG_READER;
    }

    public static InputStream readAsStream() throws Exception {
        return inputStream(CONFIG_FILE);
    }

    private static InputStream inputStream(String artifact) throws Exception {
        File yaml = getConfigFile(HOME_DIR_PROPERTY, artifact);

        if (yaml == null || !yaml.exists()) {
            yaml = getConfigFile(DEFAULT_HOME_PROPERTY, artifact);
        }

        if (yaml == null || !yaml.exists()) {
            yaml = getConfigFile(USER_HOME_PROP, artifact);
        }

        if (yaml != null && yaml.exists()) {
            return new FileInputStream(yaml);
        }

        return DefaultConfigReader.class.getClassLoader().getResourceAsStream(artifact);
    }

    private static File getConfigFile(String systemProperty, String artifactName) throws Exception {
        String applicationPath = System.getenv(systemProperty);
        if (applicationPath == null) {
            applicationPath = System.getProperty(systemProperty);
        }

        File artifact = null;
        if (applicationPath != null) {
            artifact = new File(applicationPath + File.separator + "etc" + File.separator + artifactName);
            if (!artifact.exists()) {
                artifact = new File(applicationPath + File.separator + "config" + File.separator + artifactName);
            }
        }

        if (artifact != null && artifact.exists()) {
            return artifact;
        }
        return null;
    }

    @Override
    public <T extends Configuration> T read(String id, Class<T> type) {
        Collection<T> confs = read(type);
        if (confs != null) {
            for (T conf : confs) {
                if (id.equals(conf.getID())) {
                    return conf;
                }
                if (conf.getAliases() == null) continue;
                for (String alias : conf.getAliases()) {
                    if (id.equals(alias)) {
                        return conf;
                    }
                }
            }
        }
        LOGGER.warn("No configuration found for ID: " + id + " of type: " + type);
        return null;
    }

    @Override
    public <T extends Configuration> Collection<T> read(Class<T> type) {
        try {
            if (configHolder == null) {
                synchronized (this) {
                    if (configHolder == null) {
                        InputStream stream = readAsStream();
                        configHolder = new ConfigHolder(stream);
                    }
                }
            }
            return configHolder.readValue(type);
        } catch (Exception e) {
            LOGGER.error("Error reading value of type: " + type, e);
            throw new RuntimeException("Error reading value of type: " + type, e);
        }
    }

    @Override
    public <T extends Configuration> T readSingleAny(Class<T> type) {
        Collection<T> confs = read(type);
        if (confs != null && confs.size() > 0) {
            return confs.iterator().next();
        }
        return null;
    }

}
