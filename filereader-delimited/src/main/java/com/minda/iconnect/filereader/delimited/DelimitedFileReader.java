package com.minda.iconnect.filereader.delimited;

import com.whilter.core.internal.ResponseCodeImpl;
import com.whilter.exception.ApplicationException;
import com.whilter.filereader.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mayank on 30/07/19 12:33 PM.
 */
public class DelimitedFileReader implements FileReaderService {
    private final DelimitedFileReaderEndpoint delimitedFileReaderEndpoint;

    public DelimitedFileReader(FileReaderEndpoint delimitedFileReaderEndpoint) {
        this.delimitedFileReaderEndpoint = (DelimitedFileReaderEndpoint) delimitedFileReaderEndpoint;
    }

    @Override
    public ResultSet read(InputStream input) {

        if (delimitedFileReaderEndpoint.getDelimiter() == null || delimitedFileReaderEndpoint.getDelimiter().trim().isEmpty()) {
            throw new ApplicationException(new ResponseCodeImpl("400", "Bad Request"), "Delimiter Expected");
        }

        HashMap<Integer, String> columnIndexMap = new HashMap<>();
        List<String> headers = new ArrayList<>();
        List<Row> rows = new ArrayList<>();

        AtomicInteger rowIndex = new AtomicInteger();
        final BufferedReader lineReader = new BufferedReader(new InputStreamReader(input));
        lineReader.lines().forEach(val -> {
            final String[] split = val.split(delimitedFileReaderEndpoint.getDelimiter());
            if (rowIndex.getAndIncrement() == 0) {
                prepareHeaderInformation(columnIndexMap, headers, split);
            } else {
                Map<String, String> columnNameValueMap = new HashMap<>();
                Map<Integer, String> columnIndexValueMap = new HashMap<>();

                if (!isRowEmpty(split, columnIndexMap)) {
                    int counter = 0;
                    for (String cell : split) {
                        String headerName = columnIndexMap.get(counter++);
                        Integer columnIndex = counter;
                        columnNameValueMap.put(headerName, cell);
                        columnIndexValueMap.put(columnIndex, cell);
                    }
                    rows.add(new DelimitedRow(columnNameValueMap, columnIndexValueMap));
                }
            }
        });

        return new DelimitedResultSet(headers, rows);
    }

    private void prepareHeaderInformation(HashMap<Integer, String> columnIndexMap, List<String> headers, String[] split) {
        int cellCounter = 0;
        for (String cell : split) {
            columnIndexMap.put(cellCounter ++, cell);
            headers.add(cell);
        }
    }

    boolean isRowEmpty(String[] row, HashMap<Integer, String> columnIndexMap) {
        final Integer maxIndex = columnIndexMap.keySet().stream().max(Integer::compareTo).orElse(null);
        if (maxIndex != null && row.length >= maxIndex) {
            for (Integer index : columnIndexMap.keySet()) {
                if (index != null) {
                    String value = row[index];
                    if (value != null && !value.trim().replaceAll(" ", "").isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
