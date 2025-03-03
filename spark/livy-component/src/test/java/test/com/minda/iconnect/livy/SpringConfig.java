package test.com.minda.iconnect.livy;

import com.cloudera.livy.Job;
import com.minda.iconnect.livy.Livy;
import com.minda.iconnect.livy.LivyComponent;
import com.minda.iconnect.livy.LivyEndpoint;
import com.minda.iconnect.platform.AbstractSpringConfig;
import com.minda.iconnect.platform.Bootstrap;
import com.minda.iconnect.platform.PlatformConfig;
import com.minda.iconnect.platform.conf.ConfigReader;
import com.minda.iconnect.platform.core.Component;
import com.minda.iconnect.platform.core.ComponentResolver;
import com.minda.iconnect.spark.config.LivyConf;
import com.minda.iconnect.spark.config.SparkApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Created by deepakchauhan on 22/07/17.
 */
@Import(PlatformConfig.class)
public class SpringConfig extends AbstractSpringConfig {

    @Bean
    public Livy livy(ConfigReader configReader, ComponentResolver resolver) {
        LivyConf livyConf = configReader.readSingleAny(LivyConf.class);
        SparkApplication sparkApplication = configReader.readSingleAny(SparkApplication.class);
        LivyEndpoint livyEndpoint = new LivyEndpoint(livyConf, sparkApplication);
        Component<LivyEndpoint, Livy> livyComponent = resolver.resolveAny(LivyComponent.class);
        return livyComponent.get(livyEndpoint);
    }

    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap(true, SpringConfig.class);
        Livy livy = bootstrap.getPlatformContext().get(Livy.class);

        livy.submit((Job<String>) jc -> "deepak");
    }

}
