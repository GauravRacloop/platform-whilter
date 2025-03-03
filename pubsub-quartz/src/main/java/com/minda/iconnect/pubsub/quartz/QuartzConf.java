package com.minda.iconnect.pubsub.quartz;

import com.minda.iconnect.platform.conf.ConfArray;
import com.minda.iconnect.platform.pubsub.PubsubConfiguration;
import com.minda.iconnect.platform.rdbms.RDBMS;

/**
 * Created by mayank on 10/08/18 6:08 PM.
 */
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
