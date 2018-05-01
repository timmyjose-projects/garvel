package com.tzj.garvel.core.net;

import com.tzj.garvel.core.net.api.NetworkConnector;
import com.tzj.garvel.core.net.api.NetworkConnectorFactory;
import com.tzj.garvel.core.net.api.NetworkConstants;
import com.tzj.garvel.core.net.api.NetworkService;
import com.tzj.garvel.common.buffers.DynamicBuffer;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

import java.io.*;
import java.net.HttpURLConnection;
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

            final int code = conn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new NetworkServiceException(String.format("Failed to download text file as string %s: Server returned %s\n", code));
            }

            conn.setReadTimeout(0);

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
     * @param urlString
     */
    @Override
    public void downloadBinaryFile(final String urlString, final String targetFile) throws NetworkServiceException {
        try {
            final NetworkConnector connector = NetworkConnectorFactory.getConnector();

            final URL url = new URL(urlString);
            final HttpURLConnection conn = connector.getConnection(url);

            final int code = conn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new NetworkServiceException(String.format("Failed to download binary file %s: Server returned %s\n", code));
            }
            conn.setReadTimeout(0);

            try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                 BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile))) {

                byte[] buffer = new byte[NetworkConstants.BUF_SIZE];
                int count = -1;

                while ((count = in.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
            } catch (IOException e) {
                throw e;
            }
        } catch (IOException e) {
            throw new NetworkServiceException(String.format("Failed to download binary file %s: %s\n", urlString, e.getLocalizedMessage()));
        }
    }

    /**
     * Download a text file from the given url, into the given location. To avoid issues with encoding.
     * process the stream as a byte stream instead of a character stream.
     *
     * @param urlString
     */
    @Override
    public void downloadTextFile(final String urlString, final String downloadLocation) throws NetworkServiceException {
        try {
            final String outputFile = downloadLocation
                    + File.separator + urlString.substring(urlString.lastIndexOf("/") + 1, urlString.length());
            final NetworkConnector connector = NetworkConnectorFactory.getConnector();

            final URL url = new URL(urlString);
            final HttpURLConnection conn = connector.getConnection(url);

            final int code = conn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new NetworkServiceException(String.format("Failed to download text file %s: Server returned %s\n", code));
            }

            conn.setReadTimeout(0);

            try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                 BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {

                byte[] buffer = new byte[NetworkConstants.BUF_SIZE];
                int count = -1;

                while ((count = in.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
            } catch (IOException e) {
                throw e;
            }
        } catch (IOException e) {
            throw new NetworkServiceException(String.format("Failed to download binary file %s: %s\n", urlString, e.getLocalizedMessage()));
        }
    }

    @Override
    public DynamicBuffer<Byte> downloadBinaryFileAsByteBuffer(final String url) throws NetworkServiceException {
        return null;
    }
}
