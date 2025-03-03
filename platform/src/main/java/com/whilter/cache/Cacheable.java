package com.whilter.cache;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@org.springframework.cache.annotation.Cacheable(cacheNames = "global")
public @interface Cacheable {

    @AliasFor("cacheNames")
    String[] value() default {"global"};

    @AliasFor("value")
    String[] cacheNames() default {"global"};
}
