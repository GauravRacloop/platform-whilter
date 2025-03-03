package com.minda.iconnect.pubsub.sftp;

import com.minda.iconnect.platform.conf.ConfArray;
import com.minda.iconnect.platform.pubsub.PubsubConfiguration;

/**
 * @author thanos on 22/05/19
 */
@ConfArray(SftpConf[].class)
public class SftpConf extends PubsubConfiguration {
    private String path;
    private long initialDelayMillis;
    private long delay;
    private int readlockMinAgeMinutes;
    private String readlock;
    private String moveFailed;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getInitialDelayMillis() {
        return initialDelayMillis;
    }

    public void setInitialDelayMillis(long initialDelayMillis) {
        this.initialDelayMillis = initialDelayMillis;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public int getReadlockMinAgeMinutes() {
        return readlockMinAgeMinutes;
    }

    public void setReadlockMinAgeMinutes(int readlockMinAgeMinutes) {
        this.readlockMinAgeMinutes = readlockMinAgeMinutes;
    }

    public String getReadlock() {
        return readlock;
    }

    public void setReadlock(String readlock) {
        this.readlock = readlock;
    }

    public String getMoveFailed() {
        return moveFailed;
    }

    public void setMoveFailed(String moveFailed) {
        this.moveFailed = moveFailed;
    }
}
