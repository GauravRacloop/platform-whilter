package com.minda.iconnect.livy.job.interactive;

/**
 * Created by mayank on 31/08/17.
 */
public class JsonResponse {

    private String className;
    private String json;

    public JsonResponse() {
    }

    public JsonResponse(String className, String json) {
        this.className = className;
        this.json = json;
    }

    public String getClassName() {
        return className;
    }

    public String getJson() {
        return json;
    }
}
