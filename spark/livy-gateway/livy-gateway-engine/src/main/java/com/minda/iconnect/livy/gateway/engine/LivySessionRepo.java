package com.minda.iconnect.livy.gateway.engine;

/**
 * Created by mayank on 18/09/17.
 */
public interface LivySessionRepo {

    int getSessionId(String jobName);

    void saveLivySessionMapping(int sessionId, String jobName);
}
