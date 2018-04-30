package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.BuildCommandResult;
import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.builder.api.exception.BuildException;
import com.tzj.garvel.core.builder.api.strategy.BuildContext;
import com.tzj.garvel.core.builder.strategy.BasicBuildStrategy;
import com.tzj.garvel.core.cache.exception.CacheManagerException;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverContext;
import com.tzj.garvel.core.dep.resolver.SimpleDependencyResolverStrategy;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class BuildJob implements Job<BuildCommandResult> {
    public BuildJob() {
    }

    /**
     * 1). Create the `target` directory with `build` and `deps` directories. `build` will hold the build
     * artifacts (class files basically), and the `deps` directory will hold the dependency graph of the current
     * project in binary form.The Dependency Graph will not only help generate the JAR file more quickly, but
     * also help in incremental builds where only the changed source files will be compiled (based on checking
     * the timestamp of the class files against the source file).
     * <p>
     * 2). Parse the Garvel.gl file and populate the Core Cache. If there are any changes (new jar,
     * changed version of existing jar), then contact Maven Central and then update the Garvel cache at
     * $HOME/.garvel/cache, as well as update the Dependency Graph of the project itself. The downloaded artifacts
     * will be checksummed using both MD5 and SHA1. For now, only HTTP support will be provided, which should be
     * extended to HTTPS support in later versions.
     * <p>
     * 3). Compile the project and create the deliverable JAR file, ${PROJECT_NAME}.jar in the `target` directory.
     * If there are any errors, report them to the user and exit immediately.
     * <p>
     * Note: Since the idea is to bootstrap Garvel to use Garvel itself, special handling must be done for the
     * `Garvel` project name to generate the the wrapper scripts for `garvel`.jar as well. Whether to restrict
     * new project names with the same name will depend on testing and verification.
     *
     * @return
     * @throws JobException
     */
    @Override
    public BuildCommandResult call() throws JobException {
        BuildCommandResult result = new BuildCommandResult();

        // 1. create target directory hierarchy
        createTargetHierarchy(result);

        // 2. Parse the project config file and populate the Core Cache.
        populateCoreCache();

        // 3. Analyse the dependencies.
        List<String> artifactPaths = analyseDependencies();

        // 4. Compile the project.
        // 5. generate the project artifacts
        buildProject(artifactPaths, result);

        return result;
    }

    /**
     * Step 4 - Compile the project sources into the `target/build` directory.
     * <p>
     * 1. Convert the dependencies paths to OS-specific classpath entries.
     * 2. Pass this list to the builder along with the desired compilation
     * strategy.
     * 3. Build the project.
     * <p>
     * Step 5 - Generate the project artifacts in the `target` directory.
     *
     * @param artifactPaths
     * @param result
     */
    private void buildProject(final List<String> artifactPaths, final BuildCommandResult result) throws JobException {
        String classPathString = UtilServiceImpl.INSTANCE.convertStringsToOSSpecificClassPathString(artifactPaths);
        Path jarFilePath = null;

        try {
            final BuildContext ctx = new BuildContext(new BasicBuildStrategy());
            jarFilePath = ctx.executeStrategy(classPathString);
        } catch (BuildException e) {
            throw new JobException(String.format("Project Build failed: %s\n", e.getErrorString()));
        }

        result.setJarFile(jarFilePath);
    }

    /**
     * Step 3 - Imvoke the Dependency Manager to analyse the dependencies, if any.
     */
    private List<String> analyseDependencies() throws JobException {
        List<String> dependenciesClassPath = null;
        try {
            final DependencyResolverContext ctx = new DependencyResolverContext(new SimpleDependencyResolverStrategy());
            dependenciesClassPath = CoreModuleLoader.INSTANCE.getDependencyManager().analyse(ctx);
        } catch (DependencyManagerException e) {
            throw new JobException(String.format("Dependency Analysis failed: %s\n", e.getErrorString()));
        }

        return dependenciesClassPath;
    }

    /**
     * Step 2 - Populate the Core cache by parsing the Garvel/gl file.
     *
     * @throws JobException
     */
    private void populateCoreCache() throws JobException {
        try {
            CoreModuleLoader.INSTANCE.getCacheManager().populateCoreCaches();
        } catch (CacheManagerException e) {
            throw new JobException(String.format("failed to populate Core Caches: %s\n", e.getErrorString()));
        }
    }

    /**
     * Step 1 - create the `target`, `target/build`, and `target/deps` directories, if they are not
     * present (due to a previously invoked earlier `clean` ccommand).
     *
     * @param result
     * @throws JobException
     */
    private void createTargetHierarchy(final BuildCommandResult result) throws JobException {
        Path targetDirPath = null;
        try {
            targetDirPath = createTargetDir();
        } catch (FilesystemFrameworkException e) {
            throw new JobException(String.format("failed to create `target`: %s\n", e.getErrorString()));
        }

        Path buildDirPath = null;
        try {
            buildDirPath = createBuildDir(targetDirPath);
        } catch (FilesystemFrameworkException e) {
            cleanup(targetDirPath);
            throw new JobException(String.format("failed to create `target/build`: %s\n", e.getErrorString()));
        }

        Path depsDirPath = null;

        try {
            depsDirPath = createDepsDir(targetDirPath);
        } catch (FilesystemFrameworkException e) {
            cleanup(targetDirPath, buildDirPath);
            throw new JobException(String.format("failed to create `target/build`: %s\n", e.getErrorString()));
        }

        result.setTargetDir(targetDirPath);
        result.setDepsDir(depsDirPath);
    }

    /**
     * TYhe target directory hierarchy creation is an atomic operation. Failure must entail
     * cleanup of all the already created directories.
     *
     * @param paths
     */
    private void cleanup(final Path... paths) {
        for (Path p : paths) {
            try {
                CoreModuleLoader.INSTANCE.getFileSystemFramework().deleteDirectory(p);
            } catch (FilesystemFrameworkException e) {
                // don't fail because of this.
            }
        }
    }

    private Path createDepsDir(final Path targetDirPath) throws FilesystemFrameworkException {
        final String depsDir = targetDirPath.toFile().getAbsolutePath() + File.separator + "deps";

        final Path checkDeps = CoreModuleLoader.INSTANCE.getFileSystemFramework().checkDirectoryExistsGetPath(depsDir);
        if (checkDeps != null) {
            return checkDeps;
        }

        return CoreModuleLoader.INSTANCE.getFileSystemFramework().makeDirectory(depsDir);
    }

    /**
     * In additiont to building the `build` directory, the entire directory structure of `src`
     * must be created in the `build` directory. This will help during project artifact creation
     * to include non-source files in the final JAR file. @TODO.
     *
     * @param targetDirPath
     * @return
     * @throws FilesystemFrameworkException
     */
    private Path createBuildDir(final Path targetDirPath) throws FilesystemFrameworkException {
        final String buildDir = targetDirPath.toFile().getAbsolutePath() + File.separator + "build";

        final Path checkBuild = CoreModuleLoader.INSTANCE.getFileSystemFramework().checkDirectoryExistsGetPath(buildDir);
        if (checkBuild != null) {
            return checkBuild;
        }

        return CoreModuleLoader.INSTANCE.getFileSystemFramework().makeDirectory(buildDir);
    }

    private Path createTargetDir() throws FilesystemFrameworkException {
        final String targetDir = GarvelCoreConstants.GARVEL_PROJECT_ROOT + File.separator + "target";

        final Path checkTarget = CoreModuleLoader.INSTANCE.getFileSystemFramework().checkDirectoryExistsGetPath(targetDir);
        if (checkTarget != null) {
            return checkTarget;
        }

        return CoreModuleLoader.INSTANCE.getFileSystemFramework().makeDirectory(targetDir);
    }
}
