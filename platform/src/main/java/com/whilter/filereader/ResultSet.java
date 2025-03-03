package com.whilter.filereader;

import java.util.Collection;

/**
 * Created by mayank on 30/07/19 10:54 AM.
 */
public interface ResultSet {

    Collection<String> headers();

    boolean hasNext();

    Row next();

    void reset();

}
