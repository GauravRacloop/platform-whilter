package com.minda.iconnect.livy.gateway.engine;

import com.minda.iconnect.livy.Livy;
import com.minda.iconnect.livy.LivyComponent;
import com.minda.iconnect.livy.LivyEndpoint;
import com.minda.iconnect.livy.gateway.api.ApiResponse;
import com.minda.iconnect.livy.gateway.api.LivyGatewayApi;
import com.minda.iconnect.livy.gateway.api.Parameters;
import com.minda.iconnect.livy.gateway.engine.internal.DefaultLivySessionRepo;
import com.minda.iconnect.livy.job.interactive.JsonPlatformLivyJob;
import com.minda.iconnect.platform.conf.ConfigReader;
import com.minda.iconnect.platform.core.ComponentResolver;
import com.minda.iconnect.livy.job.interactive.JsonResponse;
import com.minda.iconnect.spark.config.*;

import java.util.*;

/**
 * Created by mayank on 29/08/17.
 */
public class LivyGatewayService implements LivyGatewayApi {

    private final ComponentResolver resolver;
    private final LivyConf livyConf;
    private final LivySessionRepo livySessionRepo;
    private final Map<String, SparkApplication> jobApplicationMap;

    public LivyGatewayService(Collection<SparkApplication> sparkApps, ComponentResolver resolver, LivyConf livyConf) {
        this.resolver = resolver;
        this.livyConf = livyConf;
        livySessionRepo = new DefaultLivySessionRepo(sparkApps);
        jobApplicationMap = new HashMap<>();

        prepareCache(sparkApps);
    }

    @Override
    public <T> ApiResponse<T> executeJob(String jobName, Parameters parameters) throws Exception {
        SparkApplication sparkApplication = resolveAndValidateSparkApplication(jobName);
        Livy livy = resolveLivy(sparkApplication, jobName);
        JsonResponse resultJson = livy.submitForJsonResponse(new JsonPlatformLivyJob(jobName, prepareSparkParameters(jobName, parameters))).get();
        ApiResponse<JsonResponse> response = new ApiResponse<>();
        response.setResult(resultJson);
        return (ApiResponse<T>) response;
    }

    @Override
    public void executeBatch(String jobName, Parameters parameters) throws Exception {
        SparkApplication sparkApplication = resolveAndValidateSparkApplication(jobName);
        Livy livy = resolveLivy(sparkApplication, jobName);

        livy.executeBatchJob(jobName, prepareSparkParameters(jobName, parameters));
    }

    private com.minda.iconnect.spark.param.Parameters prepareSparkParameters(String jobName, Parameters parameters) {
        parameters.put("jobId", jobName);
        com.minda.iconnect.spark.param.Parameters target = new com.minda.iconnect.spark.param.Parameters();
        parameters.forEach(target::putIfAbsent);
        return target;
    }

    private Livy resolveLivy(SparkApplication sparkApplication, String jobName) throws Exception {
        LivyComponent livyComponent = resolver.resolveAny(LivyComponent.class);
        Livy livy = livyComponent.get(new LivyEndpoint(livyConf, sparkApplication));
        if (sparkApplication.getJobType() == SparkApplication.JobType.INTERACTIVE) {
            if (livy.getSessionById(livySessionRepo.getSessionId(jobName)) == null) {
                livySessionRepo.saveLivySessionMapping(livy.renewSession().getId(), jobName);
            }
        }
        return livy;
    }

    private SparkApplication resolveAndValidateSparkApplication(String jobName) {
        SparkApplication sparkApplication = jobApplicationMap.get(jobName);

        if (sparkApplication == null) {
            throw new IllegalArgumentException("JobName: " + jobName + " doesnot exists.");
        }
        return sparkApplication;
    }

    private void prepareCache(Collection<SparkApplication> sparkApps) {
        sparkApps.stream()
                .forEach(
                        sparkApplication -> sparkApplication.getJobs().stream().forEach(
                                job -> jobApplicationMap.put(job, sparkApplication)
                        )
                );
    }

}
