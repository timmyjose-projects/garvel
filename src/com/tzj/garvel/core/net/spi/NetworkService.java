package com.tzj.garvel.core.net.spi;

public interface NetworkService {
    void downloadTextFIle(final String url);

    void downloadBinaryFile(final String url);
}
