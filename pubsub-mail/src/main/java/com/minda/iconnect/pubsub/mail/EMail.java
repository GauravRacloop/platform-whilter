package com.minda.iconnect.pubsub.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;

/**
 * Created by mayank on 09/01/19 6:23 PM.
 */
public interface EMail {
    String to();

    String from();

    String subject();

    String cc();

    Date timeStamp();

    Collection<Attachment> attachments();

    interface Attachment {
        String fileName();

        InputStream inputStream() throws IOException;
    }
}
