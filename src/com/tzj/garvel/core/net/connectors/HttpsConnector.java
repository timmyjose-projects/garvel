package com.tzj.garvel.core.net.connectors;

import com.tzj.garvel.core.net.api.NetworkConnector;

public class HttpsConnector extends NetworkConnector {
    public HttpsConnector() {
        super();
        nextConnector = null;
    }
}
