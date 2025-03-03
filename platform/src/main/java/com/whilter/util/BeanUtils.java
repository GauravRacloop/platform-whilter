package com.whilter.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by deepakchauhan on 12/08/17.
 */
public class BeanUtils {

    public static void setProperty(Object instance, String propertyName, Object value) throws InvocationTargetException, IllegalAccessException {
        PropertyDescriptor propertyDescriptor = org.springframework.beans.BeanUtils.getPropertyDescriptor(instance.getClass(), propertyName);
        if (propertyDescriptor != null) {
            propertyDescriptor.getWriteMethod().invoke(instance, value);
        }
    }

}
