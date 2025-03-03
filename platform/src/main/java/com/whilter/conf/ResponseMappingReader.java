package com.whilter.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.whilter.conf.internal.DefaultResponseMappingReader;

/**
 * Created by mayank on 08/11/17.
 */
public interface ResponseMappingReader {

    ResponseMapping readResponseCode(String systemErrorCode);

    static ResponseMappingReader getInstance() {
        return DefaultResponseMappingReader.getInstance();
    }

    @JsonRootName(ResponseMapping.RESPONSE_CODE_MAPPING)
    class ResponseMapping {
        public static final String RESPONSE_CODE_MAPPING = "responseCodeMapping";
        public static final String HTTP_STATUS_CODE = "httpStatusCode";
        public static final String SYSTEM_ERROR_CODE = "systemErrorCode";
        public static final String MESSAGE = "message";

        @JsonProperty(HTTP_STATUS_CODE)
        private int httpStatusCode;

        @JsonProperty(SYSTEM_ERROR_CODE)
        private String systemErrorCode;

        @JsonProperty(MESSAGE)
        private String message;

        public int getHttpStatusCode() {
            return httpStatusCode;
        }

        public void setHttpStatusCode(int httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
        }

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
    }
}
