package com.tzj.garvel.core.builder.strategy;

import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.builder.api.CompilationResult;
import com.tzj.garvel.core.builder.api.compiler.Compiler;
import com.tzj.garvel.core.builder.api.compiler.CompilerFactory;
import com.tzj.garvel.core.builder.api.compiler.CompilerType;
import com.tzj.garvel.core.builder.api.exception.BuildException;
import com.tzj.garvel.core.builder.api.jar.JarFileCreator;
import com.tzj.garvel.core.builder.api.jar.JarFileCreatorFactory;
import com.tzj.garvel.core.builder.api.jar.JarFileCreatorOptions;
import com.tzj.garvel.core.builder.api.jar.JarFileCreatorType;
import com.tzj.garvel.core.builder.api.strategy.BuildStrategy;
import com.tzj.garvel.core.builder.common.AllSourceFilesFileVisitor;
import com.tzj.garvel.core.builder.common.CompilationOption;
import com.tzj.garvel.core.cache.api.*;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The basic build strategy uses the JavaxJavaCompiler for
 * compilation, and the NormalJarFileCreator for generating the
 * project artifacts.
 */
public class BasicBuildStrategy implements BuildStrategy {
    private static final String DASH = "-";
    private static final String JAR = ".jar";

    private Compiler compiler;
    private JarFileCreator jarCreator;

    public BasicBuildStrategy() {
        compiler = CompilerFactory.getCompiler(CompilerType.JAVAX_JAVACOMPILER);
        jarCreator = JarFileCreatorFactory.getJarService(JarFileCreatorType.NORMAL_JAR);
    }

    /**
     * Execute the build strategy -
     * <p>
     * 1). Compile the project
     * 2). Generate the project artifacts
     *
     * @param classPathString
     * @return
     * @throws BuildException
     */
    @Override
    public Path execute(final String classPathString) throws BuildException {
        final Path srcDirPath = Paths.get(GarvelCoreConstants.GARVEL_PROJECT_SOURCE_ROOT);
        final Path buildDirPath = Paths.get(GarvelCoreConstants.GARVEL_PROJECT_BUILD_DIR);

        // compile the project
        if (!buildDirPath.toFile().exists()) {
            throw new BuildException(String.format("build failed - the build directory (%s) does not exist\n", buildDirPath));
        }

        final CompilationResult compilationResult = compileProject(srcDirPath, classPathString, buildDirPath);
        if (!compilationResult.isSuccessful()) {
            UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "Build failed due to compilation errors");

            final List<String> errorMessages = compilationResult.getDiagnostics();
            for (final String errorMessage : errorMessages) {
                System.out.println(errorMessage);
            }

            throw new BuildException("Build failed due to compilation errors");
        }

        // generate the project artifacts
        final JarFileCreatorOptions jarFileOptions = getJarFileOptions();
        final Path jarFilePath = jarCreator.createJarFile(buildDirPath, jarFileOptions);

        // delete the build directory
        try {
            CoreModuleLoader.INSTANCE.getFileSystemFramework().deleteDirectoryHierarchy(buildDirPath);
        } catch (FilesystemFrameworkException e) {
            throw new BuildException(e.getErrorString());
        }

        return jarFilePath;
    }

    /**
     * Return the Jar file creation options for the project.
     *
     * @return
     */
    private JarFileCreatorOptions getJarFileOptions() {
        final CacheManagerService cache = CoreModuleLoader.INSTANCE.getCacheManager();

        final String jarName = getJarName(cache);
        final String manifestVersion = getManifestVersion(cache);
        final String mainClass = getMainClass(cache);

        JarFileCreatorOptions options = null;
        if (mainClass != null) {
            options = new JarFileCreatorOptions(jarName, manifestVersion, mainClass);
        } else {
            options = new JarFileCreatorOptions(jarName, manifestVersion);
        }

        return options;
    }

    /**
     * Return the Main-Class attribute from the Garvel.gl configuration file.
     * The name is normalized from com.foo.Bar to com/foo/Bar as required by
     * the JAR specification.
     * <p>
     * If this attribute is not present, skip it.
     *
     * @param cache
     * @return
     */
    private String getMainClass(final CacheManagerService cache) {
        if (cache.containsCacheKey(CacheKey.MAIN_CLASS)) {
            final MainClassEntry mainClassEntry = (MainClassEntry) cache.getEntry(CacheKey.MAIN_CLASS);
            String mainClassName = mainClassEntry.getMainClassPath();

            if (mainClassName != null && !mainClassName.isEmpty()) {
                mainClassName = mainClassName.replace(".", "/");
            }

            return mainClassName;
        }

        return null;
    }

    /**
     * Get the JAR file Manifest file version. This is simply the project version.
     *
     * @param cache
     * @return
     */
    private String getManifestVersion(final CacheManagerService cache) {
        final VersionEntry versionEntry = (VersionEntry) cache.getEntry(CacheKey.VERSION);
        return versionEntry.getVersion();
    }

    /**
     * This name must be the relative path to the project root.
     *
     * @param cache
     * @return
     */
    private String getJarName(final CacheManagerService cache) {
        final NameEntry nameEntry = (NameEntry) cache.getEntry(CacheKey.NAME);
        final String projectName = nameEntry.getName();

        final VersionEntry versionEntry = (VersionEntry) cache.getEntry(CacheKey.VERSION);
        final String version = versionEntry.getVersion();

        final String baseJarFileName = projectName + DASH + version + JAR;
        final String qualifiedJarFileName = GarvelCoreConstants.GARVEL_PROJECT_TARGET_DIR +
                File.separator + baseJarFileName;

        return qualifiedJarFileName;
    }

    /**
     * Compile the project.
     *
     * @return
     */
    private CompilationResult compileProject(final Path srcDirPath, final String classPathString, final Path buildDirPath) throws BuildException {
        final List<File> srcFiles = getSourceFilesForCompilation(srcDirPath);
        final List<String> compilationOptions = getCompilationOptions(classPathString, buildDirPath);

        final CompilationResult compilationresult = compiler.compile(buildDirPath, srcFiles, compilationOptions);

        return compilationresult;
    }

    /**
     * Generate the project JAR file.
     */
    private void generateProjectArtifacts() {

    }

    /**
     * This will walk the source root and find all the source files.
     *
     * @param srcDirPath
     * @return
     */
    private List<File> getSourceFilesForCompilation(final Path srcDirPath) throws BuildException {
        final Set<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);

        List<File> sourceFiles = new ArrayList<>();
        try {
            Files.walkFileTree(srcDirPath, opts, Integer.MAX_VALUE, new AllSourceFilesFileVisitor(sourceFiles));
        } catch (IOException e) {
            throw new BuildException(String.format("Build failed while collecting project source files: %s\n", e.getLocalizedMessage()));
        }

        return sourceFiles;
    }


    /**
     * These options can be possibly be read from the config file in future versions,
     * or be configurable by the user via the Garvel.gl configuration file.
     * Currently, these are hardcoded in.
     * Also, all the entries specified in the Garvel.gl file are added here as
     * custom classpath entries.
     *
     * @return
     */
    private List<String> getCompilationOptions(final String classPathString, final Path buildDirPath) {
        List<String> compilationOptions = new ArrayList<>();

        // general options
        compilationOptions.add(CompilationOption.XLINT.toString());
        compilationOptions.add(CompilationOption.TARGET_DIR.toString());
        compilationOptions.add(String.format(buildDirPath.toFile().getAbsolutePath()));

        // fill in the classpath entries
        // at the very end
        compilationOptions.add(CompilationOption.CLASSPATH.toString());
        // always add the current directory
        compilationOptions.add(".");

        final ClassPathEntry classpathEntry = (ClassPathEntry) CoreModuleLoader.INSTANCE.getCacheManager()
                .getEntry(CacheKey.CLASSPATH);

        if (classpathEntry.getPaths() != null) {
            final List<String> paths = classpathEntry.getPaths();
            for (String path : paths) {
                if (!compilationOptions.contains(path)) {
                    compilationOptions.add(File.pathSeparator);
                    compilationOptions.add(path);
                }
            }
        }

        return compilationOptions;
    }
}
