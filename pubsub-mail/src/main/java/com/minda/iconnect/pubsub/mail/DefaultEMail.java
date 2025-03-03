package com.minda.iconnect.pubsub.mail;

import javax.activation.DataHandler;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by mayank on 10/01/19 4:03 PM.
 */
public class DefaultEMail implements EMail {

    private final Map<String, Object> headers;
    private final ThreadLocal<SimpleDateFormat> dateFormatProvider = ThreadLocal.withInitial(() -> new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss XX"));
    private final org.apache.camel.Exchange exchange;

    public  DefaultEMail(org.apache.camel.Exchange exchange) {
        this.exchange = exchange;
        this.headers = exchange.getIn().getHeaders();
    }

    @Override
    public String to() {
        return String.valueOf(headers.get("To"));
    }

    @Override
    public String from() {
        return String.valueOf(headers.get("From"));
    }

    @Override
    public String subject() {
        return String.valueOf(headers.get("Subject"));
    }

    @Override
    public String cc() {
        return String.valueOf(headers.get("Cc"));
    }

    @Override
    public Date timeStamp() {
        if (headers.get("Date") != null) {
            String inputDateStr = String.valueOf(headers.get("Date"));
            if (!inputDateStr.trim().isEmpty()) {
                SimpleDateFormat simpleDateFormat = dateFormatProvider.get();
                try {
                    return simpleDateFormat.parse(inputDateStr);
                } catch (ParseException e) {
                    //Eat Me
                }
            }
        }
        return null;
    }

    @Override
    public Collection<Attachment> attachments() {
        return getAttachments(exchange);
    }

    public static class DefaultAttachment implements EMail.Attachment {
        private final Map.Entry<String, DataHandler> entry;

        public DefaultAttachment(Map.Entry<String, DataHandler> entry) {
            this.entry = entry;
        }

        @Override
        public String fileName() {
            return entry.getKey();
        }

        @Override
        public InputStream inputStream() throws IOException {
            return entry.getValue().getInputStream();
        }
    }

    private Collection<EMail.Attachment> getAttachments(org.apache.camel.Exchange exchange) {
        return exchange.getIn().getAttachments()
                .entrySet().stream()
                .map((Function<Map.Entry<String, DataHandler>, Attachment>) DefaultEMail.DefaultAttachment::new)
                .collect(Collectors.toList());
    }
}
