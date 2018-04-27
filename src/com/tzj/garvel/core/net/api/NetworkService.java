package com.tzj.garvel.core.net.api;

import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

public interface NetworkService {
    boolean checkUrlAvailable(final String url) throws NetworkServiceException;

    void downloadTextFile(final String url) throws NetworkServiceException;

    void downloadBinaryFile(final String url) throws NetworkServiceException;
}
