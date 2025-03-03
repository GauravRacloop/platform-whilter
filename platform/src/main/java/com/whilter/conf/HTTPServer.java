package com.whilter.conf;


import com.fasterxml.jackson.annotation.JsonValue;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by deepakchauhan on 24/05/17.
 */
@ConfArray(HTTPServer[].class)
public class HTTPServer extends Server {

    private String contextUrl;

    private String context;
    private String scheme;

    public HTTPServer() {
    }

    public HTTPServer(String contextUrl) throws MalformedURLException {
        setContextUrl(contextUrl);
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getScheme() {
        if (scheme == null) {
            scheme = "http";
        }
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getContextPath() {
        if (context == null) {
            context = "/";
        }
        return ("/" + context).replace("//", "/");
    }

    @JsonValue
    public String getContextUrl() {
        if (contextUrl == null) {
            StringBuilder urlBuilder = new StringBuilder(getScheme() + "://" + this.getHost());
            if (port != 80 || port != 883 || port != -1) {
                urlBuilder.append(":" + port);
            }
            if (context != null && !context.equals("/")) {
                urlBuilder.append("/" + context);
            }
            String url = urlBuilder.toString();
            contextUrl = url.replace("//", "/").replace(":/", "://");
        }
        return contextUrl;
    }

    public void setContextUrl(String contextUrl) throws MalformedURLException {
        this.contextUrl = contextUrl;
        URL url = new URL(contextUrl);
        scheme = url.getProtocol();
        context = url.getPath();
        port = url.getPort();
        host = url.getHost();
    }

    @Override
    public int getPort() {
        if (port == -1) {
            if (getScheme() == "http") port = 80;
            if (getScheme() == "https") port = 883;
        }
        return port;

    }
}
