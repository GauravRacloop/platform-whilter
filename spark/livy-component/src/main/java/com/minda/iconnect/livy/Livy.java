package com.minda.iconnect.livy;

import com.cloudera.livy.JobHandle;
import com.cloudera.livy.LivyClient;
import com.minda.iconnect.livy.job.interactive.JsonPlatformLivyJob;
import com.minda.iconnect.livy.job.interactive.JsonResponse;
import com.minda.iconnect.platform.core.Service;
import com.minda.iconnect.spark.config.LivyBatch;
import com.minda.iconnect.spark.config.LivySession;
import com.minda.iconnect.spark.param.Parameters;

/**
 * Created by deepakchauhan on 22/07/17.
 */
public interface Livy extends LivyClient, LivyRestApi, Service {

    JobHandle<JsonResponse> submitForJsonResponse(JsonPlatformLivyJob jsonPlatformLivyJob);

    LivyBatch executeBatchJob(String jobName, Parameters parameters) throws Exception;

    LivySession renewSession() throws Exception;
}
