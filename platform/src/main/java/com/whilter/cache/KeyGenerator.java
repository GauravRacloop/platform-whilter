package com.whilter.cache;


import java.lang.reflect.Method;

/**
 * Created by mayank on 18/12/17 3:12 PM.
 */
public class KeyGenerator implements org.springframework.cache.interceptor.KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return new Key(target.getClass().getName(), method.getName(), params);
    }

}
