package com.tzj.garvel.core.net.connectors;

import com.tzj.garvel.core.net.api.NetworkConnector;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;

public class HttpsConnector extends NetworkConnector {
    public HttpsConnector() {
        super();
        nextConnector = null;
    }

    @Override
    public HttpsURLConnection getConnection(final URL url) throws NetworkServiceException {
        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new NetworkServiceException(String.format("Unable to open Https connection to %s\n", url));
        }

        return conn;
    }
}
