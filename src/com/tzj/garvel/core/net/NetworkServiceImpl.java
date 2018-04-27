package com.tzj.garvel.core.net;

import com.tzj.garvel.core.net.api.NetworkConnector;
import com.tzj.garvel.core.net.api.NetworkConnectorFactory;
import com.tzj.garvel.core.net.api.NetworkConstants;
import com.tzj.garvel.core.net.api.NetworkService;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public enum NetworkServiceImpl implements NetworkService {
    INSTANCE;

    /**
     * Try to connect to the given URL with a set timeout to see if the link is
     * up and running.
     *
     * @return
     * @throws NetworkServiceException
     */
    @Override
    public boolean checkUrlAvailable(final String urlString) {
        try {
            final NetworkConnector connector = NetworkConnectorFactory.getConnector();
            URL url = null;
            url = new URL(urlString);
            final HttpURLConnection conn;
            try {
                conn = connector.getConnection(url);
            } catch (NetworkServiceException e) {
                return false;
            }
            conn.setConnectTimeout(NetworkConstants.CONNECT_TIMEOUT);
            conn.setReadTimeout(NetworkConstants.READ_TIMEOUT);
            conn.setRequestMethod(NetworkConstants.HEAD);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }

        return false;
    }

    /**
     * Download a text file from the given url.
     *
     * @param url
     */
    @Override
    public void downloadTextFile(final String url) throws NetworkServiceException {

    }

    /**
     * Download a binary file from the given url.
     *
     * @param url
     */
    @Override
    public void downloadBinaryFile(final String url) throws NetworkServiceException {

    }
}
