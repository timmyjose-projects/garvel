package com.tzj.garvel.core.filesystem;

import com.tzj.garvel.common.parser.GarvelConstants;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.filesystem.api.OsType;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;
import com.tzj.garvel.core.filesystem.api.FilesystemService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;

public enum FilesystemServiceImpl implements FilesystemService {
    INSTANCE;

    @Override
    public OsType getOs() throws FilesystemFrameworkException {
        OsType os = OsType.UNKNOWN;
        String osString = System.getProperty("os.name");

        if (osString == null) {
            return os;
        }

        osString = osString.toLowerCase();

        if (osString.contains("MAC")) {
            os = OsType.MACOS;
        } else if (osString.contains("Linux")) {
            os = OsType.LINUX;
        } else if (osString.contains("Windows")) {
            os = OsType.WINDOWS;
        }

        return os;
    }

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
    public boolean checkDirectoryExists(final String directory) throws FilesystemFrameworkException {
        return Paths.get(directory).toFile().exists();
    }

    @Override
    public Path makeDirectory(final String directory) throws FilesystemFrameworkException {
        Path newDirectory = null;

        try {
            final Path path = Paths.get(directory);

            if (path.toFile().exists()) {
                throw new FilesystemFrameworkException(String.format("directory already %s exists", directory));
            }

            final OsType os = getOs();
            switch (os) {

                case MACOS:
                case LINUX:
                    newDirectory = Files.createDirectory(path, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString(GarvelCoreConstants.POSIX_PERMISSIONS)));
                    break;
                case WINDOWS:
                    newDirectory = Files.createDirectory(path);
                    break;
                default:
                    throw new FilesystemFrameworkException("Unsupported OS. Aborting...");
            }
        } catch (IOException e) {
            throw new FilesystemFrameworkException(e.getLocalizedMessage());
        }

        return newDirectory;
    }

    @Override
    public void makeFile(final String filename) {

    }

    @Override
    public void makeTempFile(final String filename) {

    }
}
