package com.whilter.filereader;

/**
 * @author mayank on 29/07/20 7:30 PM
 */
public class ExcelFileReaderEndpoint extends FileReaderEndpoint {
    private final String sheetName;

    public ExcelFileReaderEndpoint(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }
}
