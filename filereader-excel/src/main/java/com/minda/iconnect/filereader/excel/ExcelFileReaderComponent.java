package com.minda.iconnect.filereader.excel;

import com.minda.iconnect.platform.core.internal.AbstractComponent;
import com.minda.iconnect.platform.filereader.ExcelFileReaderEndpoint;
import com.minda.iconnect.platform.filereader.FileReaderService;
import com.minda.iconnect.platform.filereader.FileReaderComponent;
import com.minda.iconnect.platform.filereader.FileReaderEndpoint;

/**
 * Created by mayank on 30/07/19 10:59 AM.
 */
public class ExcelFileReaderComponent extends AbstractComponent<FileReaderEndpoint, FileReaderService> implements FileReaderComponent {

    @Override
    protected FileReaderService doGet(FileReaderEndpoint endpoint) {
        return new ExcelFileReader(endpoint);
    }

    @Override
    protected boolean cache() {
        return true;
    }

    @Override
    protected String toUniqueID(FileReaderEndpoint fileReaderEndpoint) {
        ExcelFileReaderEndpoint endpoint = (ExcelFileReaderEndpoint) fileReaderEndpoint;
        if (endpoint == null || endpoint.getSheetName() == null || endpoint.getSheetName().trim().isEmpty()) {
            return ExcelFileReaderComponent.class.getName();
        }
        return endpoint.getSheetName();
    }
}
