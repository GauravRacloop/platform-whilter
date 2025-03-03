package com.minda.iconnect.livy.gateway.engine;

import com.minda.iconnect.livy.gateway.api.ApiResponse;
import com.minda.iconnect.livy.gateway.api.LivyGatewayApi;
import com.minda.iconnect.livy.gateway.api.Parameters;

/**
 * Created by mayank on 29/08/17.
 */
public class LivyGatewayEngine implements LivyGatewayApi {
    private final LivyGatewayApi restService;

    public LivyGatewayEngine(LivyGatewayApi restService) {
        this.restService = restService;
    }

    @Override
    public <T> ApiResponse<T> executeJob(String jobName, Parameters parameters) throws Exception {
        return restService.executeJob(jobName, parameters);
    }

    @Override
    public void executeBatch(String jobBame, Parameters parameters) throws Exception {
        restService.executeBatch(jobBame, parameters);
    }
}
