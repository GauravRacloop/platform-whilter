package com.whilter.conf;

import java.lang.annotation.*;

/**
 * Created by deepakchauhan on 13/07/17.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfArray {

    Class<?> value();
}
