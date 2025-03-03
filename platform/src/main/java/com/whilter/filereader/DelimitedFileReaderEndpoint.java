package com.whilter.filereader;

/**
 * @author mayank on 29/07/20 7:29 PM
 */
public class DelimitedFileReaderEndpoint extends FileReaderEndpoint {
    private final String delimiter;

    public DelimitedFileReaderEndpoint(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
