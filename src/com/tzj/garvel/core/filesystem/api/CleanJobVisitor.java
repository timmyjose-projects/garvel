package com.tzj.garvel.core.filesystem.api;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * This visitor implementation is used by `cleanJob` to recursively walk the
 * `target` directory, and delete the files and directories in a bottom-up fashion
 * (postorder traversal basically).
 * <p>
 * Finally, when all the contents of the `target`
 * directory have been deleted, the directory itself is deleted.
 */
public class CleanJobVisitor implements FileVisitor<Path> {
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        Files.deleteIfExists(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        throw new IOException(String.format("Failed to delete file %s", file.toAbsolutePath().toString()));
    }

    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        if (exc == null) {
            Files.deleteIfExists(dir);
            return FileVisitResult.CONTINUE;
        } else {
            throw exc;
        }
    }
}
