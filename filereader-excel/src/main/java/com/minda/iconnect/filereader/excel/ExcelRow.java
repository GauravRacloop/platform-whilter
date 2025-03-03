package com.minda.iconnect.filereader.excel;

import com.minda.iconnect.platform.filereader.Row;

import java.util.Map;

/**
 * Created by mayank on 30/07/19 11:59 AM.
 */
public class ExcelRow implements Row {

    private Map<String, String> columnNameValueMap;
    private Map<Integer, String> columnIndexValueMap;

    public ExcelRow(Map<String, String> columnNameValueMap, Map<Integer, String> columnIndexValueMap) {
        this.columnNameValueMap = columnNameValueMap;
        this.columnIndexValueMap = columnIndexValueMap;
    }

    @Override
    public String getColumnValue(int index) {
        return columnIndexValueMap.get(index);
    }

    @Override
    public String getColumnValue(String columnName) {
        return columnNameValueMap.get(columnName.toLowerCase());
    }

    @Override
    public String getAsString() {
        return columnNameValueMap.toString();
    }
}
