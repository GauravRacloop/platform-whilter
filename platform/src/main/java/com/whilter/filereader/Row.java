package com.whilter.filereader;

/**
 * Created by mayank on 30/07/19 10:54 AM.
 */
public interface Row {

    String getColumnValue(int index);

    String getColumnValue(String columnName);

    String getAsString();

}
