package com.minda.iconnect.aos;

import com.google.common.net.MediaType;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.SharedAccessBlobPermissions;
import com.microsoft.azure.storage.blob.SharedAccessBlobPolicy;
import com.whilter.exception.ApplicationException;
import com.whilter.storage.Storage;
import com.whilter.core.internal.AbstractService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.EnumSet;

/**
 * Created by deepakchauhan on 10/09/17.
 */
public class AzureStorage extends AbstractService implements Storage {

    private CloudBlobClient cloudBlobClient;
    private AzureStorageConf storageConf;

    public AzureStorage(AzureStorageConf storageConf) {
        this.storageConf = storageConf;
    }

    @Override
    public void upload(String container, String id, InputStream is, MediaType mediaType) {
        try {
            CloudBlockBlob blob = getBlobReference(container, id);
            blob.deleteIfExists();
            if (mediaType != null) {
                blob.getProperties().setContentType(mediaType.type() + "/" + mediaType.subtype());
            }
            blob.upload(is, -1);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String getUrl(String container, String id) {
        try {
            CloudBlockBlob blob = getBlobReference(container, id);
            SharedAccessBlobPolicy sasConstraints = new SharedAccessBlobPolicy();
            Date now = new Date();
            sasConstraints.setSharedAccessStartTime(new Date());
            sasConstraints.setSharedAccessExpiryTime(new Date(now.getTime() + storageConf.getSasTokenExpiryInSeconds() * 1000l));
            sasConstraints.setPermissions(EnumSet.of(SharedAccessBlobPermissions.READ));
            if (!storageConf.isGenerateSasToken()) {
                return blob.getUri().toString();
            }
            String sasBlobToken = blob.generateSharedAccessSignature(sasConstraints, null);
            return blob.getUri() + "?" + sasBlobToken;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public InputStream getInputStream(String container, String id) {
        try {
            String requestUrl = getUrl(container, id);
            return new URL(requestUrl).openStream();
        } catch (IOException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    @Override
    protected void doStart() throws Exception {
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConf.getConnectionString());
        this.cloudBlobClient = storageAccount.createCloudBlobClient();
    }

    @Override
    protected void doShutdown() {

    }

    private CloudBlockBlob getBlobReference(String container, String id) throws URISyntaxException, StorageException {
        return cloudBlobClient.getContainerReference(container).getBlockBlobReference(id);
    }

    @Override
    public boolean autoStart() {
        return true;
    }
}
