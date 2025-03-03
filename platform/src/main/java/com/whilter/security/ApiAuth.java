package com.whilter.security;

import java.lang.annotation.*;

/**
 * @author mayank on 04/02/20 11:38 AM
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiAuth {
    String apiName();
}
