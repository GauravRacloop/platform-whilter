package com.minda.iconnect.filereader.delimited;

import com.whilter.filereader.Row;

import java.util.Map;

/**
 * Created by mayank on 30/07/19 1:00 PM.
 */
public class DelimitedRow implements Row {
    private Map<String, String> columnNameValueMap;
    private Map<Integer, String> columnIndexValueMap;

    public DelimitedRow(Map<String, String> columnNameValueMap, Map<Integer, String> columnIndexValueMap) {
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
