package com.minda.iconnect.livy.internal;

import com.minda.iconnect.livy.LivyRestApi;
import com.minda.iconnect.livy.job.batch.BatchSparkJobRunner;
import com.minda.iconnect.platform.core.Jar;
import com.minda.iconnect.platform.core.internal.DefaultJarDependencyResolver;
import com.minda.iconnect.spark.config.*;
import com.minda.iconnect.spark.param.Parameters;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mayank on 15/09/17.
 */
public class LivyRequestsUtil {

    private final SparkApplication sparkApplication;
    private final SparkJobsDistro jobsDistro;

    public LivyRequestsUtil(SparkApplication sparkApplication, SparkJobsDistro jobsDistro) {
        this.sparkApplication = sparkApplication;
        this.jobsDistro = jobsDistro;
    }

    public LivySessionRequest prepareSessionRequest() throws Exception {
        if (!jobsDistro.getHdfsFsPath().endsWith(File.separator)) {
            jobsDistro.setLocalFsPath(jobsDistro.getHdfsFsPath() + File.separator);
        }

        Set<String> jarList = getJarListForApplication();
        String name = sparkApplication.getName();

        LivySessionRequest livySessionRequest = new LivySessionRequest();
        livySessionRequest.setName(name != null ? sparkApplication.getName() : sparkApplication.getID());
        livySessionRequest.setKind(sparkApplication.getKind().name());
        livySessionRequest.setDriverCores(sparkApplication.getDriverCores());
        livySessionRequest.setExecutorCores(sparkApplication.getExecutorCores());
        livySessionRequest.setHeartbeatTimeoutInSecond(sparkApplication.getHeartbeatTimeoutInSecond());
        livySessionRequest.setDriverMemory(sparkApplication.getDriverMemory());
        livySessionRequest.setExecutorMemory(sparkApplication.getExecutorMemory());
        livySessionRequest.setNumExecutors(sparkApplication.getNumExecutors());
        livySessionRequest.setQueue(sparkApplication.getQueue());
        livySessionRequest.setJars(jarList);

        return livySessionRequest;
    }

    public LivyBatchRequest prepareBatchRequest(String jobName, Parameters parameters) throws Exception {
        Set<String> jarList = getJarListForApplication();

        String mainClassJar = jobsDistro.getHdfsFsPath() + jobName + "-" + jobsDistro.getVersion() + ".jar";

        LivyBatchRequest livyBatchRequest = new LivyBatchRequest();
        livyBatchRequest.setClassName(BatchSparkJobRunner.class.getName());
        livyBatchRequest.setDriverCores(sparkApplication.getDriverCores());
        livyBatchRequest.setDriverMemory(sparkApplication.getDriverMemory());
        livyBatchRequest.setExecutorCores(sparkApplication.getExecutorCores());
        livyBatchRequest.setExecutorMemory(sparkApplication.getExecutorMemory());
        livyBatchRequest.setFile(mainClassJar);
        livyBatchRequest.setJars(jarList);
        livyBatchRequest.setConf(parameters);
        livyBatchRequest.setName(sparkApplication.getName());
        livyBatchRequest.setNumExecutors(sparkApplication.getNumExecutors());
        return livyBatchRequest;
    }

    private Set<String> getJarListForApplication() throws Exception {
        Set<String> jarList = new HashSet<>();
        for (String job : sparkApplication.getJobs()) {
            DefaultJarDependencyResolver dependencyResolver = new DefaultJarDependencyResolver(jobsDistro.getLocalFsPath());
            Collection<Jar> jars = dependencyResolver.resolveJars(jobsDistro.getGroupId(), job, jobsDistro.getVersion());
            Set<String> jrList = jars.stream().map(jar -> (jobsDistro.getHdfsFsPath() + jar.getFileName())).collect(Collectors.toSet());
            jarList.add(jobsDistro.getHdfsFsPath() + job + "-" + jobsDistro.getVersion() + ".jar");
            jarList.addAll(jrList);
        }
        return jarList;
    }

}
