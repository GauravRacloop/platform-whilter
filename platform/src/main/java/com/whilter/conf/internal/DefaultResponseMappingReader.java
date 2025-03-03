package com.whilter.conf.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.whilter.conf.ResponseMappingReader;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayank on 08/11/17.
 */
@SuppressWarnings("Duplicates")
public class DefaultResponseMappingReader implements ResponseMappingReader {

    private static final String DEFAULT_CONFIG = "response-mapping.yaml";
    private static final String DEFAULT_HOME_PROPERTY = "APP_HOME";
    private static final String USER_HOME_PROP = "user.home";
    private static String CONFIG_FILE = DEFAULT_CONFIG;
    private static String HOME_DIR_PROPERTY = DEFAULT_HOME_PROPERTY;

    static {
        String configFile = System.getProperty("config.name");
        if (configFile != null && !configFile.isEmpty()) {
            CONFIG_FILE = configFile;
        }

        String homeDir = System.getProperty(USER_HOME_PROP);
        if (homeDir != null && !homeDir.isEmpty()) {
            HOME_DIR_PROPERTY = homeDir;
        }
    }

    private static DefaultResponseMappingReader INSTANCE;
    private Map<String, ResponseMappingReader.ResponseMapping> RESPONSE_MAPPING_MAP;

    private DefaultResponseMappingReader() {
        readValuesFromConfigFile();
    }

    @Override
    public ResponseMapping readResponseCode(String systemErrorCode) {
        return RESPONSE_MAPPING_MAP.get(systemErrorCode);
    }

    public static DefaultResponseMappingReader getInstance() {
        if (INSTANCE == null) {
            synchronized (DefaultResponseMappingReader.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DefaultResponseMappingReader();
                }
            }
        }
        return INSTANCE;
    }

    private void readValuesFromConfigFile() {
        try {
            if (RESPONSE_MAPPING_MAP == null) {
                synchronized (this) {
                    if (RESPONSE_MAPPING_MAP == null) {
                        RESPONSE_MAPPING_MAP = new HashMap<>();
                        InputStream stream = readAsStream();
                        if (stream != null) {
                            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                            mapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);
                            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

                            Yaml yaml = new Yaml();
                            Map<?, ?> map = (Map<?, ?>) yaml.load(stream);
                            if (map.get(ResponseMapping.RESPONSE_CODE_MAPPING) != null) {
                                ResponseMapping[] responseMappings = mapper.readValue(mapper.writeValueAsString(map.get(ResponseMapping.RESPONSE_CODE_MAPPING)), ResponseMapping[].class);
                                if (responseMappings != null) {
                                    for (ResponseMapping responseMapping : responseMappings) {
                                        RESPONSE_MAPPING_MAP.put(responseMapping.getSystemErrorCode(), responseMapping);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
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

    public static void main(String[] args) {
        new DefaultResponseMappingReader().readResponseCode("U404");
    }

}
