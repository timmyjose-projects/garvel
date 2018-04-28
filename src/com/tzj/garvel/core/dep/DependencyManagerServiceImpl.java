package com.tzj.garvel.core.dep;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.cache.CacheManagerServiceImpl;
import com.tzj.garvel.core.cache.api.DependenciesEntry;
import com.tzj.garvel.core.dep.api.Artifact;
import com.tzj.garvel.core.dep.api.DependencyManagerService;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverContext;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverOperation;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.util.List;

/**
 * Thie module handles all the Dependency Management for Garvel.
 */
public enum DependencyManagerServiceImpl implements DependencyManagerService {
    INSTANCE;

    /**
     * 1. Check if the Garvel lock cache has been populated (Garvel lock is present).
     * 3. Send all dependencies to the Resolver.
     * 4. Once the dependency list order is received from the resolver, download them into
     * the Garvel Cache.
     * 5. Return the list of ordered dependencies as the classpath string.
     *
     * @param ctx
     * @return
     * @throws DependencyManagerException
     */
    @Override
    public List<String> analyse(final DependencyResolverContext ctx) throws DependencyManagerException {
        List<Artifact> artifactsOrdering = null;
        List<String> classPaths = null;

        // if the lock file exists, we need to diff the dependencies, handling
        // all the possible states of operations.
        if (CoreModuleLoader.INSTANCE.getCacheManager().isLockCachePopulated()) {
            final boolean configHasChanged = checkIfProjectConfigurationHasChanged();

            // the configuration has changed
            if (configHasChanged) {
                deleteGarvelLockFile();
                final boolean dependencyGraphExists = checkDependencyGraphExists();
                DependencyGraph dependencyGraph = null;

                if (dependencyGraphExists) {
                    artifactsOrdering = ctx.resolveStrategy(DependencyResolverOperation.UPDATE_AND_ANALYSE);
                } else {
                    artifactsOrdering = ctx.resolveStrategy(DependencyResolverOperation.CREATE_AND_ANALYSE);
                }

                classPaths = createClassPathEntries(artifactsOrdering);
                saveGarvelLockFile();
            } else {
                // config has not changed
                final boolean dependencyGraphExists = checkDependencyGraphExists();

                if (dependencyGraphExists) {
                    artifactsOrdering = ctx.resolveStrategy(DependencyResolverOperation.ANALYSE);
                } else {
                    artifactsOrdering = ctx.resolveStrategy(DependencyResolverOperation.CREATE_AND_ANALYSE);
                }

                classPaths = createClassPathEntries(artifactsOrdering);
                saveGarvelLockFile();
            }
        } else {
            // lock file does not exist -implies that the dependency graph also does not exist.
            classPaths = createClassPathEntries(artifactsOrdering);
            saveGarvelLockFile();
        }

        return classPaths;
    }

    /**
     * Create the new Garvel.lock file by copying over the current Garvel.gl file.
     */
    private void saveGarvelLockFile() throws DependencyManagerException {
        try {
            CoreModuleLoader.INSTANCE.getFileSystemFramework().copyFile(GarvelCoreConstants.GARVEL_PROJECT_LOCK_FILE, GarvelCoreConstants.GARVEl_PROJECT_CONFIG_FILE);
        } catch (FilesystemFrameworkException e) {
            throw new DependencyManagerException(String.format("dependency analysis failed: failed to create Garvel.lock file (%s)\n", e.getErrorString()));
        }
    }

    /**
     * Given the ordered list of artifacts, use them to construct the class-path entries for these
     * dependencies:
     * <p>
     * 1. Check if the Garvel Cache file exists.
     * 2. If the cache file exists, query the cache file for the dependencies.
     * 3. If all the dependencies are satisfied, construct the classpath entries using
     * the returned list of paths.
     * 4. If some or all of the dependencies are not satisfied, download those dependencies,
     * update the Garvel cache, and construct the list of classpath entries from the returned
     * list of paths.
     * 5. If the cache file itself does not exist, download all the dependencies from the repo(s),
     * and then return the list of paths which will then be used to construct the list of classpath
     * entries.
     *
     * @param artifactsOrdering
     * @return
     */
    private List<String> createClassPathEntries(final List<Artifact> artifactsOrdering) {
        return null;
    }

    /**
     * Check if the dependency.graph file exists inside target/deps.
     *
     * @return
     */
    private boolean checkDependencyGraphExists() {
        return CoreModuleLoader.INSTANCE.getFileSystemFramework().checkFileExists(GarvelCoreConstants.GARVEL_PROJECT_DEPS_FILE);
    }

    /**
     * Deletes the Garvel.lock file.
     *
     * @throws DependencyManagerException
     */
    private void deleteGarvelLockFile() throws DependencyManagerException {
        try {
            CoreModuleLoader.INSTANCE.getFileSystemFramework().deleteFile(GarvelCoreConstants.GARVEL_PROJECT_LOCK_FILE);
        } catch (FilesystemFrameworkException e) {
            throw new DependencyManagerException("dependency analysis failed: failed to delete the Garvel lock file\n");
        }
    }

    /**
     * Check if the configuration file has changed (in any way).
     *
     * @return
     */
    private boolean checkIfProjectConfigurationHasChanged() {
        final DependenciesEntry configDeps = CacheManagerServiceImpl.INSTANCE.getConfigDependencies();
        final DependenciesEntry lockDeps = CacheManagerServiceImpl.INSTANCE.getLockDependencies();

        boolean hasChanged = false;
        // some diffing logic here

        return hasChanged;
    }
}
