package com.tzj.garvel.core.net.api;

public interface NetworkService {
    void downloadTextFile(final String url);

    void downloadBinaryFile(final String url);
}
