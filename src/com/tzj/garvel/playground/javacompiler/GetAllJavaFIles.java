package com.tzj.garvel.playground.javacompiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class GetAllJavaFIles {
    public static void main(String[] args) throws IOException {
        final Path path = Paths.get("/Users/z0ltan/Code/Projects/Playground/temp/garvel_bk/garvel/src");

        if (!path.toFile().exists()) {
            throw new RuntimeException(String.format("%s does not exist", path));
        }

        List<File> files = new ArrayList<>();
        final Set<FileVisitOption> visitOptions = new HashSet<>();
        visitOptions.add(FileVisitOption.FOLLOW_LINKS);

        Path rootPath = Files.walkFileTree(path, visitOptions, Integer.MAX_VALUE, new CollectFileVisitor(files));

        if (!rootPath.toFile().exists()) {
            throw new RuntimeException(String.format("%s does not exist", path));
        }

        for (File f : files) {
            System.out.println(f.getAbsolutePath());
        }

        System.out.printf("%d source files\n", files.size());
    }
}

class CollectFileVisitor implements FileVisitor<Path> {
    private final List<File> files;

    public CollectFileVisitor(final List<File> files) {
        this.files = files;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        if (file.toFile().exists() && file.toFile().getName().endsWith(".java")) {
            files.add(file.toFile());
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        throw exc;
    }

    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}
