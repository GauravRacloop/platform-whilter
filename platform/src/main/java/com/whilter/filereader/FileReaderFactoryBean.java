package com.whilter.filereader;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author mayank on 29/07/20 7:39 PM
 */
public class FileReaderFactoryBean implements FactoryBean<FileReaderService> {

    private final FileReaderEndpoint fileReaderEndpoint;
    private final FileReaderComponent fileReaderComponent;

    public FileReaderFactoryBean(FileReaderEndpoint fileReaderEndpoint, FileReaderComponent fileReaderComponent) {
        this.fileReaderEndpoint = fileReaderEndpoint;
        this.fileReaderComponent = fileReaderComponent;
    }

    @Override
    public FileReaderService getObject() {
        return fileReaderComponent.get(fileReaderEndpoint);
    }

    @Override
    public Class<?> getObjectType() {
        return FileReaderService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
