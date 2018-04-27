package com.tzj.garvel.core.net;

import com.tzj.garvel.core.net.api.NetworkService;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

public enum NetworkServiceImpl implements NetworkService {
    INSTANCE;

    @Override
    public boolean checkUrlAvailable(final String url) throws NetworkServiceException {
        return false;
    }

    /**
     * Download a text file from the given url.
     *
     * @param url
     */
    @Override
    public void downloadTextFile(final String url) {

    }

    /**
     * Download a binary file from the given url.
     *
     * @param url
     */
    @Override
    public void downloadBinaryFile(final String url) {

    }
}
