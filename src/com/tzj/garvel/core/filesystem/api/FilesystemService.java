package com.tzj.garvel.core.filesystem.api;

import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.io.BufferedReader;
import java.nio.file.Path;

public interface FilesystemService {
    OsType getOs() throws FilesystemFrameworkException;

    BufferedReader newBufferedReader(final String filename) throws FilesystemFrameworkException;

    String loadFileAsString(final String filename) throws FilesystemFrameworkException;

    boolean checkDirectoryExists(final String directory) throws FilesystemFrameworkException;

    Path makeDirectory(final String directory) throws FilesystemFrameworkException;

    void makeFile(final String filename) throws FilesystemFrameworkException;

    void makeTempFile(final String filename) throws FilesystemFrameworkException;
}
