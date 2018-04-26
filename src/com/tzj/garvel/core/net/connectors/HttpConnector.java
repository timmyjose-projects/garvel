package com.tzj.garvel.core.net.connectors;

import com.tzj.garvel.core.net.api.NetworkConnector;

public class HttpConnector extends NetworkConnector {
    public HttpConnector() {
        super();
        nextConnector = new HttpsConnector();
    }
}
