package com.minda.iconnect.maven.configinject;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.minda.iconnect.platform.conf.internal.DefaultConfigReader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by deepakchauhan on 08/08/17.
 */
@Mojo(name = "config", defaultPhase = LifecyclePhase.INITIALIZE)
public class ConfigInjectMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            InputStream stream = DefaultConfigReader.readAsStream();
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

            Map<String, Object> value = mapper.readValue(stream, Map.class);
            System.out.println("YAML ---> PROPERTIES");
            for (Map.Entry<String, Object> entry : value.entrySet()) {
                String k = entry.getKey();
                Object v = entry.getValue();
                if (v instanceof List) {
                    List<Map<String, Object>> array = (List<Map<String, Object>>) v;
                    for (int i = 0; i < array.size(); i++) {
                        Map<String, Object> conf = array.get(i);
                        String key = k;
                        if (conf.get("id") != null) {
                            key += '.' + conf.get("id").toString();
                        } else {
                            key += '.' + i;
                        }

                        for (Map.Entry<String, Object> e : conf.entrySet()) {
                            if (e.getValue() != null) {
                                System.out.println(key + '.' + e.getKey() + "  " + e.getValue());
                                project.getProperties().put(key + '.' + e.getKey(), e.getValue());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new MojoFailureException(e.getMessage(), e);
        }
    }
}
