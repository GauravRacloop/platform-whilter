package com.minda.iconnect.livy.gateway.api;

/**
 * Created by mayank on 29/08/17.
 */
public class ApiResponse<T> {
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
