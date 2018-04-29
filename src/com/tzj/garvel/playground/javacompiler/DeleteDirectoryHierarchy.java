package com.tzj.garvel.playground.javacompiler;

import com.tzj.garvel.core.GarvelCoreConstants;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

public class DeleteDirectoryHierarchy {
    public static void main(String[] args) throws IOException {

        Files.walkFileTree(Paths.get(GarvelCoreConstants.GARVEL_DIR), EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new DeleteFileVisitor());
    }
}

class DeleteFileVisitor implements FileVisitor<Path> {
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        if (file.toFile().exists()) {
            Files.deleteIfExists(file);
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        throw exc;
    }

    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        if (dir.toFile().exists()) {
            Files.deleteIfExists(dir);
        }

        return FileVisitResult.CONTINUE;
    }
}
