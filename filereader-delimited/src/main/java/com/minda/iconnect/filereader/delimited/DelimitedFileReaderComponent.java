package com.minda.iconnect.filereader.delimited;

import com.whilter.core.internal.AbstractComponent;
import com.whilter.filereader.DelimitedFileReaderEndpoint;
import com.whilter.filereader.FileReaderService;
import com.whilter.filereader.FileReaderComponent;
import com.whilter.filereader.FileReaderEndpoint;

/**
 * Created by mayank on 30/07/19 12:26 PM.
 */
public class DelimitedFileReaderComponent extends AbstractComponent<FileReaderEndpoint, FileReaderService> implements FileReaderComponent {

    @Override
    protected FileReaderService doGet(FileReaderEndpoint endpoint) {
        return new DelimitedFileReader(endpoint);
    }

    @Override
    protected boolean cache() {
        return true;
    }

    @Override
    protected String toUniqueID(FileReaderEndpoint fileReaderEndpoint) {
        DelimitedFileReaderEndpoint endpoint = (DelimitedFileReaderEndpoint) fileReaderEndpoint;
        if (endpoint == null || endpoint.getDelimiter() == null || endpoint.getDelimiter().trim().isEmpty()) {
            return DelimitedFileReaderComponent.class.getName();
        }
        return endpoint.getDelimiter();
    }

}
