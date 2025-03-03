package com.whilter.pubsub;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by deepakchauhan on 13/08/17.
 */
public class Batch<T> implements Iterable<T> {

    private Collection<T> list;

    public Batch(Collection<T> list) {
        this.list = list;
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        list.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return list.spliterator();
    }

}
