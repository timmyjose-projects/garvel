package com.tzj.garvel.core.builder.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * This will recursively walk the source root to find all the
 * source files in the project.
 */
public class AllSourceFilesFileVisitor implements FileVisitor<Path> {
    private List<File> sourceFiles;

    public AllSourceFilesFileVisitor(final List<File> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        if (file.toFile().exists() && file.toFile().getName().endsWith(".java")) {
            sourceFiles.add(file.toFile());
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
