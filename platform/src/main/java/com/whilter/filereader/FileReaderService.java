package com.whilter.filereader;

import com.whilter.core.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mayank on 30/07/19 10:53 AM.
 */
public interface FileReaderService extends Service {

    ResultSet read(InputStream input) throws IOException;

    /**
     * Do Nothing
     */
    default void start() {
    }

    /**
     * Do Nothing
     */
    default void shutdown() {
    }

    /**
     * Doesn't matter if true or false, as it is a utility component
     */
    default boolean autoStart() {
        return false;
    }
}
