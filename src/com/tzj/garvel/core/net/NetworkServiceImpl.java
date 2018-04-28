package com.tzj.garvel.core.net;

import com.tzj.garvel.core.net.api.NetworkConnector;
import com.tzj.garvel.core.net.api.NetworkConnectorFactory;
import com.tzj.garvel.core.net.api.NetworkConstants;
import com.tzj.garvel.core.net.api.NetworkService;
import com.tzj.garvel.core.net.api.buffers.DynamicBuffer;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

import java.io.*;
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
     * Download a text file from the given url, into the given location.
     *
     * @param urlString
     */
    @Override
    public void downloadTextFile(final String urlString, final String downloadLocation) throws NetworkServiceException {
        final String filename = urlString.substring(urlString.lastIndexOf("/") + 1, urlString.length());
        final String outputFile = downloadLocation + File.separator + filename;

        try {
            final NetworkConnector connector = NetworkConnectorFactory.getConnector();

            final URL url = new URL(urlString);
            final HttpURLConnection conn = connector.getConnection(url);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                String line = null;

                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw e;
            }
        } catch (IOException e) {
            throw new NetworkServiceException(String.format("Failed to download text file %s: %s\n", urlString, e.getLocalizedMessage()));
        }
    }

    /**
     * Download the file contents as a String.
     *
     * @param urlString
     * @return
     * @throws NetworkServiceException
     */
    @Override
    public String downloadTextFileAsString(final String urlString) throws NetworkServiceException {
        final StringWriter outputFile = new StringWriter();

        try {
            final NetworkConnector connector = NetworkConnectorFactory.getConnector();

            final URL url = new URL(urlString);
            final HttpURLConnection conn = connector.getConnection(url);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(outputFile)) {
                String line = null;

                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw e;
            }
        } catch (IOException e) {
            throw new NetworkServiceException(String.format("Failed to download text file %s: %s\n", urlString, e.getLocalizedMessage()));
        }

        // remove any trailing whitespace
        return outputFile.toString().trim();
    }

    /**
     * Download a binary file from the given url, into the given location.
     *
     * @param url
     */
    @Override
    public void downloadBinaryFile(final String url, final String downloadLocation) throws NetworkServiceException {

    }

    @Override
    public DynamicBuffer<Byte> downloadBinaryFileAsByteBuffer(final String url) throws NetworkServiceException {
        return null;
    }
}
