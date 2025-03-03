package com.minda.iconnect.livy.gateway.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by mayank on 29/08/17.
 */

@Path("/livy-gateway-api")
public interface LivyGatewayApi {

    @POST
    @Path("/execute/job/{job-name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    <T> ApiResponse<T> executeJob(@PathParam("job-name") String jobName, Parameters parameters) throws Exception;

    @POST
    @Path("/execute/batch/{job-name}")
    @Consumes(MediaType.APPLICATION_JSON)
    void executeBatch(@PathParam("job-name") String jobName, Parameters parameters) throws Exception;
}
