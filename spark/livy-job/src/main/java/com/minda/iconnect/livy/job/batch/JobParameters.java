package com.minda.iconnect.livy.job.batch;

import org.apache.commons.collections.map.HashedMap;
import org.apache.spark.SparkConf;
import scala.Tuple2;

import java.util.HashMap;

/**
 * Created by mayank on 04/09/17.
 */
public class JobParameters extends HashMap<String, String> {

    @Override
    public String get(Object key) {
        String returnValue = super.get(key);

        if (returnValue == null) {
            returnValue = super.get("spark." + key);
        }
        return returnValue;
    }

    public void loadParameters(SparkConf sparkConf) {
        for (Tuple2<String, String> keyVal : sparkConf.getAll()) {
            put(keyVal._1().toString(), keyVal._2().toString());
        }
    }
}
