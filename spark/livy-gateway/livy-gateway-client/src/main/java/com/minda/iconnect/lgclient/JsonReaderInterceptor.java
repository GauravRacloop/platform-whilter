package com.minda.iconnect.lgclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minda.iconnect.livy.gateway.api.ApiResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by mayank on 31/08/17.
 */
public class JsonReaderInterceptor implements ReaderInterceptor {

    private ObjectMapper objectMapper;

    public JsonReaderInterceptor() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        Object o = context.proceed();

        if (o instanceof ApiResponse) {
            String className = (String) ((LinkedHashMap) ((ApiResponse) o).getResult()).get("className");
            String json = (String) ((LinkedHashMap) ((ApiResponse) o).getResult()).get("json");
            try {
                Class<?> resultClass = Class.forName(className);
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(objectMapper.readValue(json.getBytes(), resultClass));
                return apiResponse;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return o;
    }
}
