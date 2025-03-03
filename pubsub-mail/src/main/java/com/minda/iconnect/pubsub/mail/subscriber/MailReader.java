package com.minda.iconnect.pubsub.mail.subscriber;

import com.minda.iconnect.platform.pubsub.SimpleConsumer;
import com.minda.iconnect.pubsub.mail.DefaultEMail;
import com.minda.iconnect.pubsub.mail.EMail;
import com.minda.iconnect.pubsub.mail.MailConf;
import org.apache.camel.builder.RouteBuilder;

import java.util.UUID;

/**
 * Created by mayank on 09/01/19 5:42 PM.
 */
public class MailReader extends RouteBuilder {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FOLDER_NAME = "folderName";
    private static final String DELETE = "delete";
    private static final String UNSEEN = "unseen";
    private static final String FETCH_SIZE = "fetchSize";
    private static final String CONSUMER_USE_FIXED_DELAY = "consumer.useFixedDelay";
    private static final String CONSUMER_INITIAL_DELAY = "consumer.initialDelay";
    private static final String CONSUMER_DELAY = "consumer.delay";

    private final MailConf mailConf;
    private final String folderName;
    private final SimpleConsumer<EMail> simpleConsumer;
    private final String uniqueKey;

    public MailReader(MailConf mailConf, String folderName, SimpleConsumer simpleConsumer) {
        this.mailConf = mailConf;
        this.folderName = folderName;
        if (folderName == null || folderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid FolderName:" + folderName);
        }
        this.simpleConsumer = simpleConsumer;
        uniqueKey = UUID.randomUUID().toString();
    }

    @Override
    public void configure() {
        StringBuilder stringBuilder = getConnectionString();
        from(stringBuilder.toString())
                .routeId(getRouteId())
                .process(exchange -> {
                    simpleConsumer.consume(getExchange(exchange));
                });
    }

    private EMail getExchange(org.apache.camel.Exchange exchange) {
        return new DefaultEMail(exchange);
    }

    private StringBuilder getConnectionString() {
        long initialDelay = mailConf.getInitialDelayMillis();
        if (initialDelay <= 0L) {
            initialDelay = 1000L;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mailConf.getExchangeType().getCamelValue() + "://" + mailConf.getHost() + ":" + mailConf.getPort())
                .append("?").append(USERNAME).append("=").append(mailConf.getEmail())
                .append("&").append(PASSWORD).append("=").append(mailConf.getEmailPassword())
                .append("&").append(FOLDER_NAME).append("=").append(folderName)
                .append("&").append(DELETE).append("=").append("false")
                .append("&").append(UNSEEN).append("=").append("true")
                .append("&").append(FETCH_SIZE).append("=").append(mailConf.getMaxPollRecords())
                .append("&").append(CONSUMER_USE_FIXED_DELAY).append("=").append("true")
                .append("&").append(CONSUMER_INITIAL_DELAY).append("=").append(initialDelay)
                .append("&").append(CONSUMER_DELAY).append("=").append(mailConf.getPollingFrequencyInMs());
        return stringBuilder;
    }

    public String getRouteId() {
        return "MailReader_" + mailConf.getID() + "_" + folderName + "_" + uniqueKey;
    }

}
