package com.whilter.storage;

import com.whilter.conf.internal.AbstractConfiguration;
import com.whilter.core.Endpoint;

/**
 * Created by deepakchauhan on 10/09/17.
 */
public class StorageEndpoint extends AbstractConfiguration implements Endpoint {

    private StorageConf storageConf;

    public StorageEndpoint(StorageConf storageConf) {
        this.storageConf = storageConf;
    }

    public StorageConf getStorageConf() {
        return storageConf;
    }

    @Override
    public String toString() {
        return storageConf.getID();
    }

}


