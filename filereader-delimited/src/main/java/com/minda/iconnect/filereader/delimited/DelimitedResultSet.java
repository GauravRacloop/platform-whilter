package com.minda.iconnect.filereader.delimited;

import com.whilter.filereader.ResultSet;
import com.whilter.filereader.Row;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by mayank on 30/07/19 1:17 PM.
 */
public class DelimitedResultSet implements ResultSet {
    private Collection<String> headers;
    private Collection<Row> rows;
    private Iterator<Row> iterator;

    public DelimitedResultSet(Collection<String> headers, Collection<Row> rows) {
        this.headers = headers;
        this.rows = rows;

        if (rows != null) {
            iterator = rows.iterator();
        }
    }

    @Override
    public Collection<String> headers() {
        return headers;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Row next() {
        return iterator.next();
    }

    @Override
    public void reset() {
        iterator = rows.iterator();
    }
}
