package com.minda.iconnect.pubsub.mail;

import com.minda.iconnect.platform.conf.ConfArray;
import com.minda.iconnect.platform.pubsub.PubsubConfiguration;

/**
 * Created by mayank on 09/01/19 2:13 PM.
 */
@ConfArray(MailConf[].class)
public class MailConf extends PubsubConfiguration {

    private String mailFrom;
    private String email;
    private String emailPassword;
    private String host;
    private String port;
    private ExchangeType exchangeType;
    private long initialDelayMillis;

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public ExchangeType getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    public long getInitialDelayMillis() {
        return initialDelayMillis;
    }

    public void setInitialDelayMillis(long initialDelayMillis) {
        this.initialDelayMillis = initialDelayMillis;
    }

    public enum ExchangeType {
        POP3("pop3"), IMAP("imaps");


        private final String camelValue;

        ExchangeType(String camelValue) {
            this.camelValue = camelValue;
        }

        public String getCamelValue() {
            return camelValue;
        }
    }
}
