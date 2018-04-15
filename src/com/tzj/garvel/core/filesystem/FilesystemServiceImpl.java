package com.tzj.garvel.core.filesystem;

import com.tzj.garvel.common.parser.GarvelConstants;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;
import com.tzj.garvel.core.filesystem.api.FilesystemService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public enum FilesystemServiceImpl implements FilesystemService {
    INSTANCE;

    @Override
    public BufferedReader newBufferedReader(final String filename) throws FilesystemFrameworkException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            throw new FilesystemFrameworkException(String.format("Unable to open non-existent file %s", filename));
        }

        return reader;
    }

    @Override
    public String loadFileAsString(final String filename) throws FilesystemFrameworkException {
        StringBuffer sb = new StringBuffer();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(GarvelConstants.EOL);
            }
        } catch (FileNotFoundException e) {
            throw new FilesystemFrameworkException(String.format("Unable to open non-existent file %s", filename));
        } catch (IOException e) {
            throw new FilesystemFrameworkException(String.format("Unable to open file %s. Reason: %s", filename, e.getLocalizedMessage()));
        }

        return sb.toString();
    }

    @Override
    public void makeDirectory(final String directory) {

    }

    @Override
    public void makeFile(final String filename) {

    }

    @Override
    public void makeTempFile(final String filename) {

    }
}
