package com.minda.iconnect.aos;

import com.minda.iconnect.platform.conf.ConfArray;
import com.whilter.storage.StorageConf;

@ConfArray(AzureStorageConf[].class)
public class AzureStorageConf extends StorageConf {

    private String defaultEndpointsProtocol;
    private String accountName;
    private String accountKey;
    private String endpointSuffix;
    private long sasTokenExpiryInSeconds;
    private boolean generateSasToken;

    public String getDefaultEndpointsProtocol() {
        return defaultEndpointsProtocol;
    }

    public void setDefaultEndpointsProtocol(String defaultEndpointsProtocol) {
        this.defaultEndpointsProtocol = defaultEndpointsProtocol;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getEndpointSuffix() {
        return endpointSuffix;
    }

    public void setEndpointSuffix(String endpointSuffix) {
        this.endpointSuffix = endpointSuffix;
    }

    public long getSasTokenExpiryInSeconds() {
        return sasTokenExpiryInSeconds;
    }

    public void setSasTokenExpiryInSeconds(long sasTokenExpiryInSeconds) {
        this.sasTokenExpiryInSeconds = sasTokenExpiryInSeconds;
    }

    public boolean isGenerateSasToken() {
        return generateSasToken;
    }

    public void setGenerateSasToken(boolean generateSasToken) {
        this.generateSasToken = generateSasToken;
    }

    public String getConnectionString() {
        return new StringBuilder()
                .append("DefaultEndpointsProtocol=").append(defaultEndpointsProtocol).append(";")
                .append("AccountName=").append(accountName).append(";")
                .append("AccountKey=").append(accountKey).append(";")
                .append("EndpointSuffix=").append(endpointSuffix).append(";")
                .toString();
    }
}
