package com.tzj.garvel.core.net.api;

import com.tzj.garvel.common.buffers.DynamicBuffer;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

public interface NetworkService {
    boolean checkUrlAvailable(final String url) throws NetworkServiceException;

    void downloadTextFile(final String urlString, final String downloadLocation) throws NetworkServiceException;

    String downloadTextFileAsString(final String url) throws NetworkServiceException;

    void downloadBinaryFile(final String url, final String targetFile) throws NetworkServiceException;

    DynamicBuffer<Byte> downloadBinaryFileAsByteBuffer(final String url) throws NetworkServiceException;

}
