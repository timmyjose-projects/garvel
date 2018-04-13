package com.tzj.garvel.core.filesystem.spi;

public interface FilesystemService {
    void makeDirectory(final String directory);

    void makeFile(final String filename);

    void makeTempFile(final String filename);
}
