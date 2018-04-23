package com.tzj.garvel.playground.javacompiler;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JavaCompilationDemo {
    private static final String PROJECT_ROOT = "/Users/z0ltan/Code/Projects/Playground/temp/garvel_bk/garvel";
    private static final String PROJECT_SRC_ROOT = PROJECT_ROOT + File.separator + "src";
    private static final String TARGET_DIR = PROJECT_ROOT + File.separator + "target";
    private static final String BUILD_DIR = TARGET_DIR + File.separator + "build";
    private static final String MAIN_CLASS = "com/tzj/garvel/cli/CLI";
    private static final String JAR_FILE = TARGET_DIR + File.separator + "garvel.jar";

    public static void main(String[] args) throws IOException {
        compileToBuildDirectory();
        createJarFile();
        deleteBuildDIrectory();
    }

    private static void deleteBuildDIrectory() throws IOException {
        final Path buildDirPath = Paths.get(BUILD_DIR);

        Files.walkFileTree(buildDirPath, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
                throw exc;
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void createJarFile() throws IOException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, MAIN_CLASS);

        JarOutputStream jar = new JarOutputStream(new FileOutputStream(JAR_FILE), manifest);
        final Path root = Paths.get(BUILD_DIR);
        Set<FileVisitOption> opts = new HashSet<>();
        opts.add(FileVisitOption.FOLLOW_LINKS);

        final URI base = URI.create(root.toFile().getAbsolutePath());
        Files.walkFileTree(root, opts, Integer.MAX_VALUE, new JarFileCreatorVisitor(jar, base));
        jar.close();
    }

    private static void compileToBuildDirectory() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diags = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diags, Locale.getDefault(), Charset.forName("UTF-8"));

        final Path srcRootPath = Paths.get(PROJECT_SRC_ROOT);
        final Set<FileVisitOption> opts = new HashSet<>();
        opts.add(FileVisitOption.FOLLOW_LINKS);

        List<File> srcFiles = new ArrayList<>();
        try {
            final Path collPath = Files.walkFileTree(srcRootPath, opts, Integer.MAX_VALUE, new CollectSourceFilesVisitor(srcFiles));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterable<? extends JavaFileObject> units = fileManager.getJavaFileObjectsFromFiles(srcFiles);
        List<String> compilationOpts = Arrays.asList("-Xlint", "-d", String.format("%s", BUILD_DIR));
        final JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diags, compilationOpts, null, units);
        final boolean result = task.call();

        for (Diagnostic<? extends JavaFileObject> d : diags.getDiagnostics()) {
            System.out.format("%s:%d:%s\n", d.getSource().getName(), d.getLineNumber(), d.getMessage(Locale.getDefault()));
        }
    }
}

class CollectSourceFilesVisitor implements FileVisitor<Path> {
    private List<File> files;

    public CollectSourceFilesVisitor(final List<File> files) {
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

class JarFileCreatorVisitor implements FileVisitor<Path> {
    private static final int BUF_SIZE = 8 * 1024 * 1024;
    private final URI base;
    private JarOutputStream jarStream;

    public JarFileCreatorVisitor(final JarOutputStream jarStream, final URI base) {
        this.jarStream = jarStream;
        this.base = base;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        final URI rel = URI.create(file.toFile().getAbsolutePath());
        final URI norm = base.relativize(rel);

        JarEntry fileEntry = new JarEntry(norm.toString());
        fileEntry.setTime(file.toFile().lastModified());
        jarStream.putNextEntry(fileEntry);

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.toFile()))) {
            byte[] buffer = new byte[BUF_SIZE];
            int count = -1;

            while ((count = in.read(buffer)) != -1) {
                jarStream.write(buffer, 0, count);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            jarStream.closeEntry();
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        throw exc;
    }

    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        final URI rel = URI.create(dir.toFile().getAbsolutePath());
        final URI norm = base.relativize(rel);

        JarEntry dirEntry = new JarEntry(norm.toString());
        dirEntry.setTime(dir.toFile().lastModified());
        jarStream.putNextEntry(dirEntry);
        jarStream.closeEntry();

        return FileVisitResult.CONTINUE;
    }
}
