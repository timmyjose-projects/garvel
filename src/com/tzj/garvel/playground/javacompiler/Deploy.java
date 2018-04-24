package com.tzj.garvel.playground.javacompiler;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import org.w3c.dom.Attr;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class Deploy {
    private static final String PROJECT_ROOT = "/Users/z0ltan/Code/Projects/Playground/temp/garvel_bk/garvel";
    private static final String PROJECT_SRC_ROOT = PROJECT_ROOT + File.separator + "src";
    private static final String TARGET_DIR = PROJECT_ROOT + File.separator + "target";
    private static final String BUILD_DIR = TARGET_DIR + File.separator + "build";
    private static final String MAIN_CLASS = "com/tzj/garvel/cli/CLI";
    private static final String JAR_FILE = TARGET_DIR + File.separator + "garvel.jar";

    public static void main(String[] args) throws IOException, URISyntaxException {
        List<File> projectTree = getProjectTree();

        List<File> dirs = new ArrayList<>();
        for (File f : projectTree) {
            if (f.isDirectory()) {
                dirs.add(f);
            }
        }

        List<File> otherFiles = new ArrayList<>();
        for (File f : projectTree) {
            if (f.isFile() && !f.getName().endsWith(".java")) {
                otherFiles.add(f);
            }
        }

        List<File> srcFiles = new ArrayList<>();
        for (File f : projectTree) {
            if (f.isFile() && f.getName().endsWith(".java")) {
                srcFiles.add(f);
            }
        }

        final URI srcUri = new URI(PROJECT_SRC_ROOT);
        final Path buildDirPath = createBuildDirectory();
        createProjectTreeInBuildDirectory(dirs, buildDirPath, srcUri);
        compileJavaFilesToBuildDirectory(srcFiles, buildDirPath);
        copyResourcesToBuildDirectory(otherFiles, buildDirPath, srcUri);
        createJarFile(buildDirPath);
        deleteBuildDirectory(buildDirPath);
    }

    /**
     * Delete the `build` directory.
     *
     * @param buildDirPath
     * @throws IOException
     */
    private static void deleteBuildDirectory(final Path buildDirPath) throws IOException {
        Files.walkFileTree(buildDirPath, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                if (file.toFile().exists()) {
                    Files.delete(file);
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
                    Files.delete(dir);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Generate the JAR File.
     *
     * @param buildDirPath
     */
    private static void createJarFile(final Path buildDirPath) throws IOException {
        Manifest mf = new Manifest();
        mf.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        mf.getMainAttributes().put(Attributes.Name.MAIN_CLASS, MAIN_CLASS);

        JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(JAR_FILE), mf);
        final Set<FileVisitOption> opts = new HashSet<>();
        opts.add(FileVisitOption.FOLLOW_LINKS);

        final URI buildDirUri = URI.create(buildDirPath.toFile().getAbsolutePath());
        Files.walkFileTree(buildDirPath, opts, Integer.MAX_VALUE, new JarFileCreatorFileVisitor(jarStream, buildDirUri));
        jarStream.close();
    }

    /**
     * Copy the non-source files into their correct directories.
     *
     * @param otherFiles
     * @param buildDirPath
     * @param srcUri
     */
    private static void copyResourcesToBuildDirectory(final List<File> otherFiles, final Path buildDirPath, final URI srcUri) throws IOException {
        for (File f : otherFiles) {
            final URI fileUri = URI.create(f.getAbsolutePath());
            final Path newFilePath = Paths.get(buildDirPath.toFile().getAbsolutePath() + File.separator + srcUri.relativize(fileUri));

            Files.copy(Paths.get(f.getAbsolutePath()), newFilePath, StandardCopyOption.COPY_ATTRIBUTES);
        }
    }

    /**
     * Compile the Java source files to their correct directories inside the `build` directory.
     *
     * @param srcFiles
     * @param buildDirPath
     */
    private static void compileJavaFilesToBuildDirectory(final List<File> srcFiles, final Path buildDirPath) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diags = new DiagnosticCollector<>();
        StandardJavaFileManager manager = compiler.getStandardFileManager(diags, Locale.getDefault(), Charset.forName("UTF-8"));

        Iterable<? extends JavaFileObject> units = manager.getJavaFileObjectsFromFiles(srcFiles);
        List<String> compilationOptions = Arrays.asList("-Xlint", "-d", String.format(buildDirPath.toFile().getAbsolutePath()));
        final JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diags, compilationOptions, null, units);
        task.call();

        // report any compilation errors.
        if (diags.getDiagnostics().size() != 0) {
            for (Diagnostic<? extends JavaFileObject> d : diags.getDiagnostics()) {
                System.out.printf("%s:%d:%s\n", d.getSource().getName(), d.getLineNumber(), d.getSource().toUri());
            }
            System.err.println("Build failed.");
            System.exit(1);
        }
    }

    /**
     * Create the project tree structure in the `build` directory inside ``target`.
     *
     * @param dirs
     * @param buildDirPath
     * @param srcUri
     * @throws IOException
     */
    private static void createProjectTreeInBuildDirectory(final List<File> dirs, final Path buildDirPath, final URI srcUri) throws IOException {
        for (File d : dirs) {
            final URI newDirUri = URI.create(d.getAbsolutePath());
            final Path newDirPath = Paths.get(buildDirPath.toFile().getAbsolutePath() + File.separator + srcUri.relativize(newDirUri));
            Files.createDirectories(newDirPath);
        }
    }

    /**
     * Create the `build` directory inside the `target` directory.
     *
     * @return
     * @throws IOException
     */
    private static Path createBuildDirectory() throws IOException {
        return Files.createDirectory(Paths.get(BUILD_DIR));
    }

    private static List<File> getProjectTree() throws IOException {
        List<File> tree = new ArrayList<>();

        final Path srcPath = Paths.get(PROJECT_SRC_ROOT);
        final Set<FileVisitOption> opts = new HashSet<>();
        opts.add(FileVisitOption.FOLLOW_LINKS);

        Files.walkFileTree(srcPath, opts, Integer.MAX_VALUE, new AllFilesFileVisitor(tree));

        return tree;
    }
}

class JarFileCreatorFileVisitor implements FileVisitor<Path> {
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
        URI newdirUri = URI.create(dir.toFile().getAbsolutePath());

        if (dir.toFile().exists()) {
            JarEntry newDirEntry = new JarEntry(buildDirUri.relativize(newdirUri).toString());
            newDirEntry.setTime(dir.toFile().lastModified());
            jarStream.putNextEntry(newDirEntry);
            jarStream.closeEntry();
        }

        return FileVisitResult.CONTINUE;
    }
}

class AllFilesFileVisitor implements FileVisitor<Path> {

    private final List<File> tree;

    public AllFilesFileVisitor(final List<File> tree) {
        this.tree = tree;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        tree.add(file.toFile());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        throw exc;
    }

    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        tree.add(dir.toFile());
        return FileVisitResult.CONTINUE;
    }
}
