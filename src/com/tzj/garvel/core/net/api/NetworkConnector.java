package com.tzj.garvel.core.net.api;

import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

import java.net.HttpURLConnection;
import java.net.URL;

public abstract class NetworkConnector {
    protected NetworkConnector nextConnector;

    public abstract HttpURLConnection getConnection(final URL url) throws NetworkServiceException;
}
