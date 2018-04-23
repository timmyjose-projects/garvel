package com.tzj.garvel.core.filesystem;

import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.filesystem.api.OsType;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;
import com.tzj.garvel.core.filesystem.api.FilesystemService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;

import static com.tzj.garvel.common.parser.GarvelConstants.EOL;

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

        if (osString.contains("mac")) {
            os = OsType.MACOS;
        } else if (osString.contains("linux")) {
            os = OsType.LINUX;
        } else if (osString.contains("windows")) {
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
                sb.append(EOL);
            }
        } catch (FileNotFoundException e) {
            throw new FilesystemFrameworkException(String.format("Unable to open non-existent file \"%s\"", filename));
        } catch (IOException e) {
            throw new FilesystemFrameworkException(String.format("Unable to open file \"%s\", %s", filename, e.getLocalizedMessage()));
        }

        return sb.toString();
    }

    @Override
    public boolean checkDirectoryExists(final String directory) {
        return Paths.get(directory).toFile().exists();
    }

    @Override
    public boolean checkFileExists(final String filename) {
        return Paths.get(filename).toFile().exists();
    }

    @Override
    public Path makeDirectory(final String directory) throws FilesystemFrameworkException {
        Path newDirectory = null;

        try {
            final Path path = Paths.get(directory);

            if (path.toFile().exists()) {
                throw new FilesystemFrameworkException(String.format("directory \"%s\" already exists", directory));
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
    public Path makeFile(final String filename) throws FilesystemFrameworkException {
        Path newFile = null;

        try {
            final Path path = Paths.get(filename);

            if (path.toFile().exists()) {
                throw new FilesystemFrameworkException(String.format("file \"%s\" already exists", filename));
            }

            final OsType os = getOs();
            switch (os) {

                case MACOS:
                case LINUX:
                    newFile = Files.createFile(path, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString(GarvelCoreConstants.POSIX_PERMISSIONS)));
                    break;
                case WINDOWS:
                    newFile = Files.createFile(path);
                    break;
                default:
                    throw new FilesystemFrameworkException("Unsupported OS. Aborting...");
            }
        } catch (IOException e) {
            throw new FilesystemFrameworkException(e.getLocalizedMessage());
        }

        return newFile;
    }

    @Override
    public String loadClassPathFileAsString(final String classpathFile) throws FilesystemFrameworkException {
        StringBuffer sb = new StringBuffer();

        String line = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(classpathFile)))) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(EOL);
            }
        } catch (IOException e) {
            throw new FilesystemFrameworkException(String.format("Unable to load classpath file \"%s\"", classpathFile));
        }

        return sb.toString();
    }

    @Override
    public Path makeFileWithContents(final String filename, final String contents) throws FilesystemFrameworkException {
        Path newFile = makeFile(filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            String[] lines = contents.split("\n");
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new FilesystemFrameworkException(String.format("Unable to create file \"%s\"", filename));
        }

        return newFile;
    }
}
