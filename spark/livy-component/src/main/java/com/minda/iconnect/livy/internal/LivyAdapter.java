package com.minda.iconnect.livy.internal;


import com.cloudera.livy.Job;
import com.cloudera.livy.JobHandle;
import com.cloudera.livy.LivyClient;
import com.cloudera.livy.LivyClientBuilder;
import com.minda.iconnect.livy.Livy;
import com.minda.iconnect.livy.LivyEndpoint;
import com.minda.iconnect.livy.LivyRestApi;
import com.minda.iconnect.livy.job.interactive.JsonPlatformLivyJob;
import com.minda.iconnect.livy.job.interactive.JsonResponse;
import com.minda.iconnect.platform.core.internal.AbstractService;
import com.minda.iconnect.spark.config.*;
import com.minda.iconnect.spark.param.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.util.concurrent.Future;

/**
 * Created by mayank on 28/08/17.
 */
public class LivyAdapter extends AbstractService implements Livy {

    private static final Logger LOGGER = LoggerFactory.getLogger(LivyAdapter.class);

    private LivyClient livyClient;
    private LivyRequestsUtil livyRequestsUtil;

    private final LivyRestApi livyRestClient;
    private final LivyEndpoint livyEndpoint;

    public LivyAdapter(LivyEndpoint livyEndpoint, SparkJobsDistro jobsDistro, LivyRestApi livyRestApi) {
        this.livyEndpoint = livyEndpoint;
        livyRequestsUtil = new LivyRequestsUtil(livyEndpoint.getApplication(), jobsDistro);
        livyRestClient = livyRestApi;
    }

    @Override
    public <T> JobHandle<T> submit(Job<T> job) {
        return livyClient.submit(job);
    }

    @Override
    public JobHandle<JsonResponse> submitForJsonResponse(JsonPlatformLivyJob jsonPlatformLivyJob) {
        return submit(jsonPlatformLivyJob);
    }

    @Override
    public LivyBatch executeBatchJob(String jobName, Parameters parameters) throws Exception {
        return livyRestClient.executeBatch(livyRequestsUtil.prepareBatchRequest(jobName, parameters));
    }

    @Override
    public LivySession renewSession() throws Exception {
        LivySession livySession = livyRestClient.createSession(livyRequestsUtil.prepareSessionRequest());
        syncLivyClient(livySession);
        return livySession;
    }

    @Override
    public <T> Future<T> run(Job<T> job) {
        return livyClient.run(job);
    }

    @Override
    public void stop(boolean shutdownContext) {
        livyClient.stop(shutdownContext);
    }

    @Override
    public Future<?> uploadJar(File jar) {
        return livyClient.uploadJar(jar);
    }

    @Override
    public Future<?> addJar(URI uri) {
        return livyClient.addJar(uri);
    }

    @Override
    public Future<?> uploadFile(File file) {
        return livyClient.uploadFile(file);
    }

    @Override
    public Future<?> addFile(URI uri) {
        return livyClient.addFile(uri);
    }

    @Override
    protected void doStart() throws Exception {
    }
    
    private void syncLivyClient(LivySession livySession) {
        try {
            String livyUrl = livyEndpoint.getLivyConf().getContextUrl();
            if (!livyUrl.endsWith("/")) {
                livyUrl = livyUrl + "/";
            }
            livyClient = new LivyClientBuilder().setURI(new URI(livyUrl + "sessions/" + livySession.getId())).build();

        } catch (Exception ex) {
            LOGGER.error("Error while initiating Livy", ex);
            throw new RuntimeException("Cannot Start Livy: ", ex);
        }
    }

    @Override
    protected void doShutdown() {
        try {
            livyClient.stop(true);
        } catch (Exception ex) {
            LOGGER.error("Error while stopping LivyClient", ex);
        }
    }

    @Override
    public LivySession createSession(LivySessionRequest request) {
        return livyRestClient.createSession(request);
    }

    @Override
    public LivySessions getAllSessions() {
        return livyRestClient.getAllSessions();
    }

    @Override
    public LivySession getSessionById(int sessionId) {
        try {
            return livyRestClient.getSessionById(sessionId);
        } catch (Exception ex) {
            LOGGER.error("Unable to find Session with sessionId : " + sessionId, ex);
        }
        return null;
    }

    @Override
    public LivyBatch executeBatch(LivyBatchRequest livyBatchRequest) {
        return livyRestClient.executeBatch(livyBatchRequest );
    }

    @Override
    public LivyBatches getAllBatches() {
        return livyRestClient.getAllBatches();
    }

    @Override
    public LivyBatch getBatchById(int batchId) {
        try {
            return livyRestClient.getBatchById(batchId);
        } catch (Exception ex) {
            LOGGER.error("Unable to find Batch with batchId : " + batchId, ex);
        }
        return null;
    }

    @Override
    public void deleteBatch(int batchId) {
        livyRestClient.deleteBatch(batchId);
    }


    @Override
    public boolean autoStart() {
        return true;
    }

}
