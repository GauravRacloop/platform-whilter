package com.whilter.storage;

import com.google.common.net.MediaType;
import com.whilter.core.Service;

import java.io.InputStream;

/**
 * Created by deepakchauhan on 10/09/17.
 */
public interface Storage extends Service {

    void upload(String container, String id, InputStream is, MediaType mediaType);

    String getUrl(String container, String id);

    InputStream getInputStream(String container, String id);
}
