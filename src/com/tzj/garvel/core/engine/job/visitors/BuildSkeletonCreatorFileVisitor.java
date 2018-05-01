package com.tzj.garvel.core.engine.job.visitors;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * This useful visitor will be used to create the entire project structure (under the `src` directory) in the
 * destination director, `target/build`. This will also copy over non-source files into their respective
 * directories in preparation for project artifact generation.
 */
public class BuildSkeletonCreatorFileVisitor implements FileVisitor<Path> {
    private static final String JAVA = ".java";

    private final Path basePath;
    private final String buildDirPath;

    public BuildSkeletonCreatorFileVisitor(final Path basePath, final String buildDirPath) {
        this.basePath = basePath;
        this.buildDirPath = buildDirPath;
    }

    /**
     * Create the target directories before copying over the directory contents.
     *
     * @param dir
     * @param attrs
     * @return
     * @throws IOException
     */
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        if (dir != null && dir.toFile().exists()) {
            final Path targetDirRelPath = basePath.relativize(dir);
            final String targetDir = buildDirPath + File.separator + targetDirRelPath.toString();

            try {
                CoreModuleLoader.INSTANCE.getFileSystemFramework().makeDirectoryHierarchy(targetDir.toString());
            } catch (FilesystemFrameworkException e) {
                throw new IOException(String.format("Unable to create directory %s: %s\n",
                        targetDir.toString(), e.getLocalizedMessage()));
            }
        }

        return FileVisitResult.CONTINUE;
    }

    /**
     * Copy over the non-source file to the the correct directory inside `target/build`.
     *
     * @param file
     * @param attrs
     * @return
     * @throws IOException
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        if (file != null && file.toFile().exists() && !file.toFile().getName().endsWith(JAVA)) {
            final Path targetFileRelPath = basePath.relativize(file);
            final String targetFile = buildDirPath + File.separator + targetFileRelPath.toString();

            try {
                CoreModuleLoader.INSTANCE.getFileSystemFramework().copyFile(targetFile.toString(), file.toString());
            } catch (FilesystemFrameworkException e) {
                throw new IOException(String.format("Unable to copy file %s to %s: %s\n",
                        file.toString(), targetFile.toString(), e.getLocalizedMessage()));
            }
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        throw exc;
    }

    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        if (exc != null) {
            throw exc;
        }

        return FileVisitResult.CONTINUE;
    }
}
