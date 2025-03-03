package com.whilter.core.internal;

import com.whilter.core.ResponseCode;

/**
 * Created by mayank on 09/10/17.
 */
public class ResponseCodeImpl implements ResponseCode {

    private String code;
    private String message;

    public ResponseCodeImpl(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

}
