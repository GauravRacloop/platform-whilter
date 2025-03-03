package com.minda.iconnect.jaxrs.jersey.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.minda.iconnect.platform.conf.ResponseMappingReader;
import com.minda.iconnect.platform.conf.ResponseMappingReader.ResponseMapping;
import com.minda.iconnect.platform.core.ResponseCode;
import com.minda.iconnect.platform.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Created by deepakchauhan on 11/08/17.
 */
@Provider
@Priority(Priorities.USER + 1000)
public class JerseyExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JerseyExceptionMapper.class);
    private final ResponseMappingReader reader;

    public JerseyExceptionMapper() {
        reader = ResponseMappingReader.getInstance();
    }

    @Override
    public Response toResponse(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        int httpStatus = status.getStatusCode();
        String message = status.getReasonPhrase();
        String description = null;
        String systemErrorCode = null;

        if (exception instanceof WebApplicationException) {
            httpStatus = ((WebApplicationException) exception).getResponse().getStatus();
            message = ((WebApplicationException) exception).getResponse().getStatusInfo().getReasonPhrase();
            description = ((WebApplicationException) exception).getResponse().getStatusInfo().getFamily().name();
        }

        if (exception instanceof ApplicationException) {
            ApplicationException applicationException = (ApplicationException) exception;

            ResponseCode responseCode = applicationException.getResponseCode();
            systemErrorCode = responseCode.code();
            message = responseCode.message() == null ? message : responseCode.message();
            description = applicationException.getDescription() == null ? description : applicationException.getDescription();

            ResponseMapping responseMapping = reader.readResponseCode(systemErrorCode);
            if (responseMapping != null) {
                message = responseMapping.getMessage();
                httpStatus = responseMapping.getHttpStatusCode();
            }
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(message);
        apiResponse.setSystemErrorCode(systemErrorCode);
        apiResponse.setDescription(description);

        return Response.status(httpStatus).header("content-type", MediaType.APPLICATION_JSON).entity(apiResponse).build();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ApiResponse {
        private String systemErrorCode;
        private String message;
        private String description;

        public String getSystemErrorCode() {
            return systemErrorCode;
        }

        public void setSystemErrorCode(String systemErrorCode) {
            this.systemErrorCode = systemErrorCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}