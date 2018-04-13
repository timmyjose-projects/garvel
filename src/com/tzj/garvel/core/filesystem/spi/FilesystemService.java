package com.tzj.garvel.core.filesystem.spi;

import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.io.BufferedReader;

public interface FilesystemService {
    BufferedReader newBufferedReader(final String filename) throws FilesystemFrameworkException;

    String loadFileAsString(final String filename) throws FilesystemFrameworkException;

    void makeDirectory(final String directory) throws FilesystemFrameworkException;

    void makeFile(final String filename) throws FilesystemFrameworkException;

    void makeTempFile(final String filename) throws FilesystemFrameworkException;
}
