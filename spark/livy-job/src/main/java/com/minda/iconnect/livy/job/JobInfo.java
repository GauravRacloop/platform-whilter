package com.minda.iconnect.livy.job;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.Properties;

/**
 * Created by mayank on 04/09/17.
 */
public class JobInfo {

    private Properties jobProperties;
    private Class<?> jobClass;
    private Class<?> springConfigClass;
    private boolean isStreamingJob;

    public Properties getJobProperties() {
        return jobProperties;
    }

    public Class<?> getJobClass() {
        return jobClass;
    }

    public Class<?> getSpringConfigClass() {
        return springConfigClass;
    }

    public boolean isStreamingJob() {
        return isStreamingJob;
    }

    public JobInfo loadJobInfo(String jobId) {
        try {
            ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            Resource[] providerMapping = patternResolver.getResources("classpath*:META-INF/spark/" + jobId);
            jobProperties = new Properties();

            for (Resource mapping : providerMapping) {
                jobProperties.load(mapping.getInputStream());
            }

            springConfigClass = Class.forName(jobProperties.getProperty("spring-config"));
            jobClass = Class.forName(jobProperties.getProperty("class-name"));
            isStreamingJob = Boolean.valueOf(jobProperties.getProperty("streaming", "false"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return this;
    }

}
