package com.whilter.cache;

import com.whilter.conf.ConfArray;
import com.whilter.conf.internal.AbstractConfiguration;

@ConfArray(CacheConfig[].class)
public class CacheConfig extends AbstractConfiguration {

    public static final String GLOBAL_CACHE = "global";

    private String name;
    private int maxSize = Integer.MAX_VALUE;
    private int ttl = Integer.MAX_VALUE;;
    private int idle = Integer.MAX_VALUE;;
    private EvictionPolicy evictionPolicy;

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public int getIdle() {
        return idle;
    }

    public void setIdle(int idle) {
        this.idle = idle;
    }

    public EvictionPolicy getEvictionPolicy() {
        return evictionPolicy;
    }

    public void setEvictionPolicy(EvictionPolicy evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
    }

    public String getName() {
        if (name == null) {
            name = this.getID();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum EvictionPolicy {
        LRU, FIFO, LIFO
    }
}
