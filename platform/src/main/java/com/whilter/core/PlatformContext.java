package com.whilter.core;

/**
 * Created by deepakchauhan on 07/07/17.
 */
public interface PlatformContext {

    <T> T get(Class<T> type);

    <T> T get(String name, Class<T> type);
}
