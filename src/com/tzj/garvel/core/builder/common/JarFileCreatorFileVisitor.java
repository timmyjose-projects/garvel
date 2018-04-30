package com.tzj.garvel.core.builder.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * This Visitor walks the entire tree of compiled classes, and
 * constructs the required JAR file.
 */
public class JarFileCreatorFileVisitor implements FileVisitor<Path> {
    private static final int BUF_SIZE = 8 * 1024 * 1024;
    private final URI buildDirUri;
    private JarOutputStream jarStream;

    public JarFileCreatorFileVisitor(final JarOutputStream jarStream, final URI buildDirUri) {
        this.jarStream = jarStream;
        this.buildDirUri = buildDirUri;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        URI newfileUri = URI.create(file.toFile().getAbsolutePath());

        if (file.toFile().exists()) {
            JarEntry newFileEntry = new JarEntry(buildDirUri.relativize(newfileUri).toString());
            newFileEntry.setTime(file.toFile().lastModified());
            jarStream.putNextEntry(newFileEntry);

            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.toFile()))) {
                int count = -1;
                byte[] buffer = new byte[BUF_SIZE];
                while ((count = in.read(buffer)) != -1) {
                    jarStream.write(buffer, 0, count);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                jarStream.closeEntry();
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

        URI newdirUri = URI.create(dir.toFile().getAbsolutePath());

        if (!dir.toFile().exists()) {
            JarEntry newDirEntry = new JarEntry(buildDirUri.relativize(newdirUri).toString());
            newDirEntry.setTime(dir.toFile().lastModified());
            jarStream.putNextEntry(newDirEntry);
            jarStream.closeEntry();
        }

        return FileVisitResult.CONTINUE;
    }
}
