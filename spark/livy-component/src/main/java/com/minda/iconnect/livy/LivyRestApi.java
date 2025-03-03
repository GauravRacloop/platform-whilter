package com.minda.iconnect.livy;

import com.minda.iconnect.spark.config.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * Created by deepakchauhan on 22/07/17.
 */
public interface LivyRestApi {

    @POST
    @Path("/sessions/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    LivySession createSession(LivySessionRequest request);

    @GET
    @Path("/sessions/")
    @Produces(MediaType.APPLICATION_JSON)
    LivySessions getAllSessions();

    @GET
    @Path("/sessions/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    LivySession getSessionById(@PathParam("sessionId") int sessionId);

    @POST
    @Path("/batches")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    LivyBatch executeBatch(LivyBatchRequest livyBatchRequest);

    @GET
    @Path("/batches")
    @Produces(MediaType.APPLICATION_JSON)
    LivyBatches getAllBatches();

    @GET
    @Path("/batches/{batchId}")
    @Produces(MediaType.APPLICATION_JSON)
    LivyBatch getBatchById(@PathParam("batchId") int batchId);

    @DELETE
    @Path("/batches/{batchId}")
    void deleteBatch(@PathParam("batchId") int batchId);

}
