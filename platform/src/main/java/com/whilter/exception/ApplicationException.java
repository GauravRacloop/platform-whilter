package com.whilter.exception;

import com.whilter.core.ResponseCode;
import com.whilter.core.internal.ResponseCodeImpl;

/**
 * Created by mayank on 09/10/17.
 */
public class ApplicationException extends RuntimeException {

    private ResponseCode responseCode;
    private String description;

    public ApplicationException() {
        super("Internal Server Error");
    }

    public ApplicationException(String message) {
        super(message);
        responseCode = new ResponseCodeImpl("500", message);
    }

    public ApplicationException(ResponseCode responseCode) {
        super(responseCode.message());
        this.responseCode = responseCode;
    }

    public ApplicationException(ResponseCode responseCode, String description) {
        this(responseCode);
        this.description = description;
    }

    public ApplicationException(String responseCode, String message, String description) {
        this(new ResponseCodeImpl(responseCode, message), description);
    }

    public ApplicationException(ResponseCode responseCode, Throwable cause) {
        super(cause);
        this.responseCode = responseCode;
    }

    public ApplicationException(String message, Throwable cause) {
        this(new ResponseCodeImpl("500", message), cause);
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public String getDescription() {
        return description;
    }
}
