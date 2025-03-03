package com.minda.iconnect.pubsub.sftp.subscriber;

import com.minda.iconnect.platform.pubsub.SimpleConsumer;
import com.minda.iconnect.pubsub.sftp.SftpExchange;
import com.minda.iconnect.pubsub.sftp.SftpConf;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author thanos on 21/05/19
 */
public class SftpReader extends RouteBuilder {
    private final SftpConf sftpConf;
    private final SimpleConsumer simpleConsumer;

    private static final String CONSUMER_USE_FIXED_DELAY = "consumer.useFixedDelay";
    private static final String CONSUMER_INITIAL_DELAY = "consumer.initialDelay";
    private static final String CONSUMER_DELAY = "consumer.delay";
    private static final String READ_LOCK = "readLock";
    private static final String READ_LOCK_MIN_AGE = "readLock";
    private static final String MOVE_FAILED = "moveFailed";

    public SftpReader(SftpConf sftpConf, SimpleConsumer simpleConsumer) {
        this.sftpConf = sftpConf;
        this.simpleConsumer = simpleConsumer;

    }

    @Override
    public void configure() {
        from(getPollingUrl())
                .process(line ->
                        simpleConsumer.consume(getExchange(line))
                )
                .to(getDestinationUrl());
    }

    private Object getExchange(Exchange line) throws FileNotFoundException {
        File file = line.getIn().getBody(File.class);
        InputStream fis = new FileInputStream(file);
        return new SftpExchange(file.getName(), fis);
    }

    private String getPollingUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("file://").append(sftpConf.getPath())
                .append("?").append(CONSUMER_USE_FIXED_DELAY).append("=").append(true)
                .append("&").append(CONSUMER_INITIAL_DELAY).append("=").append(sftpConf.getInitialDelayMillis())
                .append("&").append(CONSUMER_DELAY).append("=").append(sftpConf.getDelay())
                .append("&").append(READ_LOCK).append("=").append(sftpConf.getReadlock())
                .append("&").append(READ_LOCK_MIN_AGE).append("=").append(sftpConf.getReadlockMinAgeMinutes())
                .append("&").append(MOVE_FAILED).append("=").append(sftpConf.getMoveFailed())
                .toString();
    }

    private String getDestinationUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("file://").append(sftpConf.getPath()).append("/").append("processed").toString();
    }
}
