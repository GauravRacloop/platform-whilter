package com.minda.iconnect.livy.gateway.engine;

import com.minda.iconnect.livy.LivyRestApi;
import com.minda.iconnect.livy.gateway.api.LivyGatewayApi;
import com.minda.iconnect.platform.AbstractSpringConfig;
import com.minda.iconnect.platform.PlatformConfig;
import com.minda.iconnect.platform.conf.ConfigReader;
import com.minda.iconnect.platform.conf.ServiceConf;
import com.minda.iconnect.platform.core.Component;
import com.minda.iconnect.platform.core.ComponentResolver;
import com.minda.iconnect.platform.core.Jar;
import com.minda.iconnect.platform.core.Service;
import com.minda.iconnect.platform.core.internal.DefaultJarDependencyResolver;
import com.minda.iconnect.platform.jaxrs.JaxRSClient;
import com.minda.iconnect.platform.jaxrs.JaxRSClientComponent;
import com.minda.iconnect.platform.jaxrs.JaxRSEndpoint;
import com.minda.iconnect.platform.jaxrs.JaxRSServerComponent;
import com.minda.iconnect.platform.runner.Runner;
import com.minda.iconnect.spark.config.LivyConf;
import com.minda.iconnect.spark.config.SparkApplication;
import com.minda.iconnect.spark.config.SparkJobsDistro;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.apache.hadoop.conf.Configuration;

import javax.inject.Named;
import java.io.File;
import java.util.Collection;

/**
 * Created by mayank on 29/08/17.
 */
@Import(PlatformConfig.class)
public class SpringConfig extends AbstractSpringConfig {

    public static void main(String[] args) throws Exception {
        Runner.main(new String[]{"--spring", "com.minda.iconnect.livy.gateway.engine.SpringConfig"});
    }

    @Bean(name = "lgEngine")
    public LivyGatewayEngine lgEngine(@Named("lgService") LivyGatewayApi lgService,
                                      @Named("livy-gateway") ServiceConf serviceConf,
                                      ComponentResolver resolver) throws Exception {

        LivyGatewayEngine lgEngine = new LivyGatewayEngine(lgService);

        JaxRSEndpoint endpoint = new JaxRSEndpoint(LivyGatewayApi.class, serviceConf, lgEngine);
        JaxRSServerComponent component = resolver.resolveAny(JaxRSServerComponent.class);
        Service server = component.get(endpoint);
        server.start();
        return lgEngine;
    }

    @Bean(name = "lgService")
    public LivyGatewayApi lgService(ConfigReader configReader,
                                    ComponentResolver resolver,
                                    @Named("iconnect-livy-conf") LivyConf livyConf) {
        return new LivyGatewayService(configReader.read(SparkApplication.class), resolver, livyConf);
    }

    @Bean(name = "batchPurgeService")
    public BatchPurgeService batchPurgeService(@Named("livyRestApiClient") LivyRestApi livyRestApi,
                                               @Named("iconnect-livy-conf") LivyConf livyConf) {
        return new BatchPurgeService(livyRestApi, livyConf);
    }


    @Bean(name = "livyRestApiClient")
    public LivyRestApi livyRestApiClient(ComponentResolver resolver,
                                         @Named("iconnect-livy-conf") LivyConf livyConf) {
        Component<JaxRSEndpoint, JaxRSClient> component = resolver.resolveAny(JaxRSClientComponent.class);
        ServiceConf conf = new ServiceConf();
        conf.setHosts(new LivyConf[]{livyConf});
        conf.setLoadBalancer(livyConf);
        JaxRSEndpoint jaxRSEndpoint = new JaxRSEndpoint(LivyRestApi.class, conf);

        if (jaxRSEndpoint.getService().getID() == null) {
            jaxRSEndpoint.getService().setID(livyConf.getID() + "-Service");
        }
        JaxRSClient<LivyRestApi> rsClient = component.get(jaxRSEndpoint);

        return rsClient.proxy();
    }

    @Autowired
    public void loadJarsToHDFS(@Named ("spark-distribution") SparkJobsDistro sjDistro) throws Exception {

        String hadoopConfDir = System.getenv("HADOOP_CONF_DIR");

        if (hadoopConfDir == null) {
            throw new RuntimeException("unable to resolve environment variable HADOOP_CONF_DIR");
        }

        DefaultJarDependencyResolver dependencyResolver = new DefaultJarDependencyResolver(sjDistro.getLocalFsPath());

        Collection<Jar> jars = dependencyResolver.resolveJars(
                sjDistro.getGroupId(),
                sjDistro.getArtifactId(),
                sjDistro.getVersion());

        if (!hadoopConfDir.endsWith(File.separator)) {
            hadoopConfDir = hadoopConfDir + File.separator;
        }

        Configuration configuration = new Configuration();
        configuration.addResource(new Path(hadoopConfDir + "core-site.xml"));
        configuration.addResource(new Path(hadoopConfDir + "hdfs-site.xml"));

        Path[] sourceJars = new Path[jars.size()];

        int counter = 0;
        for (Jar jar : jars) {
            sourceJars[counter++] = new Path(jar.getFilePath());
        }

        FileSystem fs = FileSystem.get(configuration);

        if (!fs.exists(new Path(sjDistro.getHdfsFsPath()))) {
            fs.mkdirs(new Path(sjDistro.getHdfsFsPath()));
        }

        boolean deleteSource = false;
        boolean overWrite = true;

        fs.copyFromLocalFile(deleteSource, overWrite, sourceJars, new Path(sjDistro.getHdfsFsPath()));

        fs.close();
    }
}
