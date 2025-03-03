package com.minda.iconnect.pubsub.eventhubs;

import com.minda.iconnect.aos.AzureStorageConf;
import com.minda.iconnect.platform.conf.ConfArray;
import com.minda.iconnect.platform.pubsub.PubsubConfiguration;

@ConfArray(EventHubsConf[].class)
public class EventHubsConf extends PubsubConfiguration {

    private String namespace;
    private String hostName;
    private String sharedAccessKeyName;
    private String sharedAccessKey;
    private String entityPath;
    private String endpointName;
    private AzureStorageConf azureStorage;
    private String storageContainer;
    private boolean iothub;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getSharedAccessKeyName() {
        return sharedAccessKeyName;
    }

    public void setSharedAccessKeyName(String sharedAccessKeyName) {
        this.sharedAccessKeyName = sharedAccessKeyName;
    }

    public String getSharedAccessKey() {
        return sharedAccessKey;
    }

    public void setSharedAccessKey(String sharedAccessKey) {
        this.sharedAccessKey = sharedAccessKey;
    }

    public String getEntityPath() {
        return entityPath;
    }

    public void setEntityPath(String entityPath) {
        this.entityPath = entityPath;
    }

    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }

    public AzureStorageConf getAzureStorage() {
        return azureStorage;
    }

    public void setAzureStorage(AzureStorageConf azureStorage) {
        this.azureStorage = azureStorage;
    }

    public String getStorageContainer() {
        return storageContainer;
    }

    public void setStorageContainer(String storageContainer) {
        this.storageContainer = storageContainer;
    }

    public boolean isIothub() {
        return iothub;
    }

    public void setIothub(boolean iothub) {
        this.iothub = iothub;
    }

    public String getServiceConnectionString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HostName=").append(hostName).append(";")
                .append("SharedAccessKeyName=").append(sharedAccessKeyName).append(";")
                .append("SharedAccessKey=").append(sharedAccessKey).append(";");

        return builder.toString();
    }

}
