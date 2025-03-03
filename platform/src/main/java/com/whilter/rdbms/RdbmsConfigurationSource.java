package com.whilter.rdbms;

/**
 * Created by deepakchauhan on 03/09/17.
 */
public interface RdbmsConfigurationSource {

    String confId();
    String persistenceUnit();
    String[] packagesToScan();
    boolean enableJpa();
}
