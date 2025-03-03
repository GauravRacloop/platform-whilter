package com.minda.iconnect.livy.gateway.engine.internal;

import com.minda.iconnect.livy.gateway.engine.LivySessionRepo;
import com.minda.iconnect.spark.config.SparkApplication;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayank on 18/09/17.
 */
public class DefaultLivySessionRepo implements LivySessionRepo {

    private final Map<String, String> jobAppMap;
    private final Map<String, Integer> sparkAppSessionIdMap;

    public DefaultLivySessionRepo(Collection<SparkApplication> sparkApps) {
        jobAppMap = new HashMap<>();
        sparkAppSessionIdMap = new HashMap<>();

        sparkApps.stream()
                .forEach(
                        sparkApplication -> sparkApplication.getJobs().stream().forEach(
                                job -> jobAppMap.put(job, sparkApplication.getName())
                        )
                );
    }

    @Override
    public int getSessionId(String jobName) {
        String sparkAppName = jobAppMap.get(jobName);
        Integer value = sparkAppSessionIdMap.get(sparkAppName);
        return value == null ? -1 : value;
    }

    @Override
    public void saveLivySessionMapping(int sessionId, String jobName) {
        sparkAppSessionIdMap.put(jobAppMap.get(jobName), sessionId);
    }

}
