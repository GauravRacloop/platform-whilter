package com.whilter.core;

/**
 * Created by deepakchauhan on 15/07/17.
 */
public interface ComponentResolver extends Service {

    <T extends Component> T resolve(String name, Class<T> type);

    <T extends Component> T resolveAny(Class<T> type);

}
