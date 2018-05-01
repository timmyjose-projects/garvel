package com.tzj.garvel.playground.javacompiler;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

public class CreateProjectSkeleton {
    public static void main(String[] args) {
        final Path basePath = Paths.get(GarvelCoreConstants.GARVEL_PROJECT_SOURCE_ROOT);

        // get the skeleton of the project, and create it inside the
        // build directory.
        final String buildDirPath = "/Users/z0ltan/Code/Projects/Playground/testbed/foo/build";

        try {
            Files.walkFileTree(basePath, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new DirectorySkeletonCreatorFileVisitor(basePath, buildDirPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class DirectorySkeletonCreatorFileVisitor implements FileVisitor<Path> {
    private final Path basePath;
    private final String buildDirPath;

    public DirectorySkeletonCreatorFileVisitor(final Path basePath, final String buildDirPath) {
        this.basePath = basePath;
        this.buildDirPath = buildDirPath;
    }

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

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        if (file != null && file.toFile().exists() && !file.toFile().getName().endsWith(".java")) {
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
