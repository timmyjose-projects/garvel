package com.tzj.garvel.core.filesystem;

import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.filesystem.api.CleanJobVisitor;
import com.tzj.garvel.core.filesystem.api.OsType;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;
import com.tzj.garvel.core.filesystem.api.FilesystemService;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.EnumSet;

import static com.tzj.garvel.common.parser.GarvelConstants.EOL;

public enum FilesystemServiceImpl implements FilesystemService {
    INSTANCE;

    /**
     * Simplistic way to get the OS vendor. We do not care
     * as much for the specific version.
     *
     * @return
     * @throws FilesystemFrameworkException
     */
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

    /**
     * Return a new BufferedReader for the given filename (if valid).
     *
     * @param filename
     * @return
     * @throws FilesystemFrameworkException
     */
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

    /**
     * Load the file contents into a String object.
     *
     * @param filename
     * @return
     * @throws FilesystemFrameworkException
     */
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

    /**
     * Check if directory represented by the name exists. If so,
     * return that path.
     *
     * @param directory
     * @return
     */
    @Override
    public Path checkDirectoryExistsGetPath(final String directory) {
        final Path dirPath = Paths.get(directory);

        if (!dirPath.toFile().exists()) {
            return null;
        }

        return dirPath;
    }

    /**
     * Check if directory represented by the name exists.
     *
     * @param directory
     * @return
     */
    @Override
    public boolean checkDirectoryExists(final String directory) {
        return Paths.get(directory).toFile().exists();
    }

    /**
     * Check if the file represented by the name exists. If so,
     * return that path.
     *
     * @param filename
     * @return
     */
    @Override
    public Path checkFileExistsGetPath(final String filename) {
        final Path filePath = Paths.get(filename);

        if (!filePath.toFile().exists()) {
            return null;
        }

        return filePath;
    }

    /**
     * Check if the file represented by the name exists.
     *
     * @param filename
     * @return
     */
    @Override
    public boolean checkFileExists(final String filename) {
        return Paths.get(filename).toFile().exists();
    }


    /**
     * Create the directory at the path specified by the name.
     *
     * @param directory
     * @return
     * @throws FilesystemFrameworkException
     */
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

    /**
     * Create the whole hierarchy of directories, creating
     * non-existent ones along the way.
     *
     * @param directory
     * @return
     * @throws FilesystemFrameworkException
     */
    @Override
    public Path makeDirectoryHierarchy(final String directory) throws FilesystemFrameworkException {
        Path rootDirectory = null;
        final Path directoryPath = Paths.get(directory);

        try {
            final OsType os = getOs();
            switch (os) {

                case MACOS:
                case LINUX:
                    rootDirectory = Files.createDirectories(directoryPath, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString(GarvelCoreConstants.POSIX_PERMISSIONS)));
                    break;
                case WINDOWS:
                    rootDirectory = Files.createDirectories(directoryPath);
                    break;
                default:
                    throw new FilesystemFrameworkException("Unsupported OS. Aborting...");
            }
        } catch (IOException e) {
            throw new FilesystemFrameworkException(e.getLocalizedMessage());
        }

        return rootDirectory;
    }

    /**
     * Create the file at the path specified by the name.
     *
     * @param filename
     * @return
     * @throws FilesystemFrameworkException
     */
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

    /**
     * Delete the given directory, if possible.
     *
     * @param directory
     */
    @Override
    public void deleteDirectory(final String directory) throws FilesystemFrameworkException {
        try {
            Files.deleteIfExists(Paths.get(directory));
        } catch (IOException e) {
            throw new FilesystemFrameworkException(String.format("Unable to delete directory \"%s\": %s\n", directory, e.getLocalizedMessage()));
        }
    }

    /**
     * Delete the given directory (supplied as a Path), if possible.
     *
     * @param path
     */
    @Override
    public void deleteDirectory(final Path path) throws FilesystemFrameworkException {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FilesystemFrameworkException(String.format("Unable to delete directory \"%s\": %s\n", path.toString(), e.getLocalizedMessage()));
        }
    }

    /**
     * Delete the entire directory hierarchy by walking the tree recursively and deleting all
     * entries in post-order fashion.
     *
     * @param path
     * @throws FilesystemFrameworkException
     */
    @Override
    public void deleteDirectoryHierarchy(final Path path) throws FilesystemFrameworkException {
        if (!path.toFile().exists()) {
            return;
        }

        try {
            Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new CleanJobVisitor());
        } catch (IOException e) {
            throw new FilesystemFrameworkException(String.format("Unable to delete directory hierarchy starting at \"%s\": %s\n", path.toString(), e.getLocalizedMessage()));
        }
    }

    /**
     * Delete the given file, if possible.
     *
     * @param filename
     */
    @Override
    public void deleteFile(final String filename) throws FilesystemFrameworkException {
        try {
            Files.deleteIfExists(Paths.get(filename));
        } catch (IOException e) {
            throw new FilesystemFrameworkException(String.format("Unable to delete file \"%s\": %s\n", filename, e.getLocalizedMessage()));
        }
    }

    /**
     * Load a Garvel project system resource as a String object.
     *
     * @param classpathFile
     * @return
     * @throws FilesystemFrameworkException
     */
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

    /**
     * Create the file with the given name and given contents.
     *
     * @param filename
     * @param contents
     * @return
     * @throws FilesystemFrameworkException
     */
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

    /**
     * Copy the source file to the destination file.
     *
     * @param destinationFile
     * @param sourceFile
     * @return
     * @throws FilesystemFrameworkException
     */
    @Override
    public void copyFile(final String destinationFile, final String sourceFile) throws FilesystemFrameworkException {
        final Path sourceFilePath = Paths.get(sourceFile);

        if (!sourceFilePath.toFile().exists()) {
            throw new FilesystemFrameworkException(String.format("file \"%s\" does not exist", sourceFile));
        }

        final Path destinationFilePath = Paths.get(destinationFile);

        try {
            Files.copy(sourceFilePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FilesystemFrameworkException(String.format("file \"%s\" could not be copied to \"%s\": %s",
                    sourceFile, destinationFile, e.getLocalizedMessage()));
        }

        // sanity check
        if (!destinationFilePath.toFile().exists()) {
            throw new FilesystemFrameworkException(String.format("file \"%s\" could not be copied to \"%s\": internal error",
                    sourceFile, destinationFile));
        }
    }

    /**
     * Deserialize the stored object and return the instance.
     *
     * @param filename
     * @param clazz
     * @return
     */
    @Override
    public <T> T loadSerializedObject(final String filename, final Class<T> clazz) throws FilesystemFrameworkException {
        T object = null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            object = clazz.cast(in.readObject());
        } catch (IOException e) {
            throw new FilesystemFrameworkException(String.format("failed to load binary object in %s\n", filename));
        } catch (ClassNotFoundException e) {
            throw new FilesystemFrameworkException(String.format("failed to load binary object in %s\n", filename));
        }

        return object;
    }

    /**
     * Serialize and store the object as the specified filename.
     *
     * @param object
     * @param filename
     * @throws FilesystemFrameworkException
     */
    @Override
    public void storeSerializedObject(final Object object, final String filename) throws FilesystemFrameworkException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(object);
        } catch (IOException e) {
            throw new FilesystemFrameworkException(String.format("failed to save binary object in %s\n", filename));
        }
    }
}
