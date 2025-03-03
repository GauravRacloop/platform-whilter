package com.whilter.cache;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by mayank on 18/12/17 3:12 PM.
 */
public class Key implements Serializable {

    private static final long serialVersionUID = 2L;

    private String className;
    private String methodName;
    private Object[] parameters;

    public Key(String className, String methodName, Object[] parameters) {
        this.className = className;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key that = (Key) o;

        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }
}
