package com.tzj.garvel.playground.javacompiler;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class Tree {
    public static void main(String[] args) throws IOException {
        final Path path = Paths.get("/Users/z0ltan/Code/Projects/Playground/temp/garvel_bk/garvel/target/build");
        final String skip = "build";

        final Set<FileVisitOption> opts = new HashSet<>();
        opts.add(FileVisitOption.FOLLOW_LINKS);

        Files.walkFileTree(path, opts, Integer.MAX_VALUE, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                System.out.printf("Visiting file %s\n", file.toFile().getAbsolutePath());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
                throw exc;
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
                if (path.toFile().getName().equalsIgnoreCase(skip)) {
                    return FileVisitResult.CONTINUE;
                }

                System.out.printf("Visiting directory %s\n", dir.toFile().getAbsolutePath());
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
