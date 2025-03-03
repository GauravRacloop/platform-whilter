package com.minda.iconnect.aos;

import com.whilter.storage.StorageComponent;
import com.whilter.core.internal.AbstractComponent;
import com.whilter.storage.StorageEndpoint;

/**
 * Created by deepakchauhan on 10/09/17.
 */
public class BlobStorageComponent extends AbstractComponent<StorageEndpoint, AzureStorage> implements StorageComponent<StorageEndpoint, AzureStorage> {

    @Override
    protected AzureStorage doGet(StorageEndpoint endpoint) {
        return new AzureStorage((AzureStorageConf) endpoint.getStorageConf());
    }

    @Override
    protected boolean cache() {
        return true;
    }
}
