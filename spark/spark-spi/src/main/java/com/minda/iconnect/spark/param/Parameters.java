package com.minda.iconnect.spark.param;

import java.util.HashMap;

/**
 * Created by mayank on 25/08/17.
 */
public class Parameters extends HashMap<String, String> {

    @Override
    public String get(Object key) {
        String returnValue = super.get(key);

        if (returnValue == null) {
            returnValue = super.get("spark." + key);
        }
        return returnValue;
    }
}
