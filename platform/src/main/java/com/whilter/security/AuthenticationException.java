package com.whilter.security;

/**
 * Created by deepakchauhan on 11/08/17.
 */
public class AuthenticationException extends RuntimeException {

    private String realm = null;

    public AuthenticationException(String message, String realm) {
        super(message);
        this.realm = realm;
    }

    public String getRealm() {
        return this.realm;
    }
}
