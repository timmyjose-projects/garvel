package com.tzj.garvel.core.net.connectors;

import com.tzj.garvel.core.net.api.NetworkConnector;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpConnector extends NetworkConnector {
    public HttpConnector() {
        super();
        nextConnector = new HttpsConnector();
    }

    @Override
    public HttpURLConnection getConnection(final URL url) throws NetworkServiceException {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new NetworkServiceException(String.format("Unable to open Http connection to %s\n", url));
        }

        return conn;
    }
}
