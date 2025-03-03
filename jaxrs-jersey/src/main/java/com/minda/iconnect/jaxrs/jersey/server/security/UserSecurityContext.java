package com.minda.iconnect.jaxrs.jersey.server.security;

import com.minda.iconnect.platform.security.Role;
import com.minda.iconnect.platform.security.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by deepakchauhan on 11/08/17.
 */
public class UserSecurityContext implements SecurityContext {

    private User user;

    public UserSecurityContext(User user) {
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        for (Role r : user.getRoles()) {
            if (r.getName().equals(role)) return true;
        }
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
