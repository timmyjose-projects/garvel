package com.tzj.garvel.core.net.api;

import com.tzj.garvel.core.net.connectors.HttpConnector;

/**
 * The default scheme for now is to try HTTP first,
 * and failing that, trying HTTPS.
 */
public class NetworkConnectorFactory {
    private NetworkConnectorFactory() {
    }

    public static NetworkConnector getConnector() {
        return new HttpConnector();
    }
}
