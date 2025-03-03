package com.minda.iconnect.pubsub.sftp;

import java.io.InputStream;

/**
 * @author thanos on 23/05/19
 */
public class SftpExchange {

    private String name;

    private InputStream inputStream;

    public SftpExchange(String name, InputStream inputStream) {
        this.name = name;
        this.inputStream = inputStream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
