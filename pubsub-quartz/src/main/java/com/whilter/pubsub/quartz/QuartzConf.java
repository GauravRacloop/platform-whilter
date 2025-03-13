package com.whilter.pubsub.quartz;

import com.whilter.conf.ConfArray;
import com.whilter.pubsub.PubsubConfiguration;
import com.whilter.rdbms.RDBMS;


@ConfArray(QuartzConf[].class)
public class QuartzConf extends PubsubConfiguration {
    private RDBMS rdbms;
    private String tablePrefix;

    public RDBMS getRdbms() {
        return rdbms;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public void setRdbms(RDBMS rdbms) {
        this.rdbms = rdbms;
    }
}
