package com.tzj.garvel.core.dep;

import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.cache.CacheManagerServiceImpl;
import com.tzj.garvel.core.cache.api.DependenciesEntry;
import com.tzj.garvel.core.dep.api.Artifact;
import com.tzj.garvel.core.dep.api.DependencyManagerService;
import com.tzj.garvel.core.dep.api.cache.GarvelCache;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.exception.DependencyResolverException;
import com.tzj.garvel.core.dep.api.exception.RepositoryLoaderException;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoader;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoaderFactory;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverContext;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverOperation;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;
import com.tzj.garvel.core.parser.api.visitor.semver.SemverKey;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
            // lock file does not exist - implies that the dependency graph also does not exist.
            artifactsOrdering = ctx.resolveStrategy(DependencyResolverOperation.CREATE_AND_ANALYSE);
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
    private List<String> createClassPathEntries(final List<Artifact> artifactsOrdering) throws DependencyManagerException {
        final boolean garvelCacheExists = CoreModuleLoader.INSTANCE.getFileSystemFramework().checkFileExists(GarvelCoreConstants.GARVEL_CACHE_FILE);

        // cache file exists
        if (garvelCacheExists) {
            GarvelCache garvelCache = null;
            try {
                garvelCache = CoreModuleLoader.INSTANCE
                        .getFileSystemFramework()
                        .loadSerializedObject(GarvelCoreConstants.GARVEL_CACHE_FILE, GarvelCache.class);
            } catch (FilesystemFrameworkException e) {
                // fallback scheme
                return createNewCache(artifactsOrdering);
            }

            List<Artifact> unavailableArtifacts = new ArrayList<>();
            for (final Artifact artifact : artifactsOrdering) {
                if (!garvelCache.artifactAvailable(artifact)) {
                    unavailableArtifacts.add(artifact);
                }
            }

            // all dependencies are locally available
            if (!unavailableArtifacts.isEmpty()) {
                // download those dependencies which are not locally available,
                // update the Garvel Cache, and retrieve the class paths.
                List<String> downloadedPaths = downloadDependencies(unavailableArtifacts);
                updateGarvelCache(garvelCache, unavailableArtifacts, downloadedPaths);
            }

            store(garvelCache);
            return garvelCache.getPaths(artifactsOrdering);
        } else {
            return createNewCache(artifactsOrdering);
        }
    }

    /**
     * Handle the case where the Garvel Cache does not exist.
     *
     * @param artifactsOrdering
     * @throws DependencyResolverException
     */
    private List<String> createNewCache(final List<Artifact> artifactsOrdering) throws DependencyManagerException {
        // cache file does not exist
        List<String> downloadedPaths = downloadDependencies(artifactsOrdering);
        final GarvelCache garvelCache = createGarvelCache();

        updateGarvelCache(garvelCache, artifactsOrdering, downloadedPaths);
        store(garvelCache);
        return garvelCache.getPaths(artifactsOrdering);
    }

    /**
     * Create a fresh copy of the Garvel Cache.
     *
     * @return
     */
    private GarvelCache createGarvelCache() {
        return new GarvelCache();
    }

    /**
     * Update the Garvel Cache with the new information, overwriting old data if
     * necessary.
     *
     * @param garvelCache
     * @param unavailableArtifacts
     * @param downloadedPaths
     */
    private void updateGarvelCache(final GarvelCache garvelCache, final List<Artifact> unavailableArtifacts, final List<String> downloadedPaths) {
        for (int i = 0; i < unavailableArtifacts.size(); i++) {
            garvelCache.addArtifact(unavailableArtifacts.get(i), downloadedPaths.get(i));
        }
    }

    /**
     * Persist the new or update Garvel Cache onto the File System.
     *
     * @param garvelCache
     * @throws DependencyManagerException
     */
    private void store(final GarvelCache garvelCache) throws DependencyManagerException {
        try {
            CoreModuleLoader.INSTANCE.getFileSystemFramework().storeSerializedObject(garvelCache, GarvelCoreConstants.GARVEL_CACHE_FILE);
        } catch (FilesystemFrameworkException e) {
            throw new DependencyManagerException(String.format("dependency analysis failed: failed to save Garvel Cache: %s\n", e.getErrorString()));
        }
    }

    /**
     * Download the project dependencies into the Garvel Cache, creating
     * the required directories on the fly.
     *
     * @param artifactsOrdering
     */
    private List<String> downloadDependencies(final List<Artifact> artifactsOrdering) throws DependencyResolverException {
        final RepositoryLoader repoLoader = RepositoryLoaderFactory.getLoader();
        final List<String> downloadedPaths = new ArrayList<>();

        for (final Artifact artifact : artifactsOrdering) {
            final String modGroupId = artifact.getGroupId().replace(".", File.separator);
            final String directoryPath = GarvelCoreConstants.GARVEL_CACHE_DIR + File.separator +
                    modGroupId + File.separator + artifact.getArtifactId() +
                    File.separator + artifact.getVersion();

            // create the dependency hierarchy
            try {
                CoreModuleLoader.INSTANCE.getFileSystemFramework().makeDirectoryHierarchy(directoryPath);
            } catch (FilesystemFrameworkException e) {
                throw new DependencyResolverException(String.format("failed to create Garvel Cache: %s", e.getErrorString()));
            }

            // download the JAR file
            try {
                final String jarFileUrl = repoLoader.constructJARFileUrl(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
                final String jarFilePath = directoryPath + File.separator + jarFileUrl
                        .substring(jarFileUrl.lastIndexOf("/") + 1, jarFileUrl.length())
                        .replace("pom", "jar");

                CoreModuleLoader.INSTANCE.getNetworkFramework().downloadBinaryFile(jarFileUrl, jarFilePath);
                downloadedPaths.add(jarFilePath);

                UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "Finished downloading dependency %s", artifact.toString());
            } catch (RepositoryLoaderException e) {
                throw new DependencyResolverException(String.format("failed to download project dependency %s: %s",
                        artifact, e.getErrorString()));
            } catch (NetworkServiceException e) {
                throw new DependencyResolverException(String.format("failed to download project dependency %s: %s",
                        artifact, e.getErrorString()));
            }
        }

        return downloadedPaths;
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

        // sanity check
        if (configDeps == null || lockDeps == null) {
            return true;
        }

        final Map<String, Map<SemverKey, List<String>>> configMapping = configDeps.getDependencies();
        final Map<String, Map<SemverKey, List<String>>> lockMapping = lockDeps.getDependencies();

        final List<String> sanitizedConfigDeps = sanitize(configMapping);
        final List<String> sanitizedLockDeps = sanitize(lockMapping);

        // check insertion/deletion
        if (sanitizedConfigDeps.size() != sanitizedLockDeps.size()) {
            return true;
        }

        // check version change
        if (!sanitizedConfigDeps.equals(sanitizedLockDeps)) {
            return true;
        }

        return false;
    }

    private List<String> sanitize(final Map<String, Map<SemverKey, List<String>>> mapping) {
        List<String> flat = new ArrayList<>();

        StringBuffer sb = null;
        for (Map.Entry<String, Map<SemverKey, List<String>>> entry : mapping.entrySet()) {
            sb = new StringBuffer();
            sb.append(entry.getKey());

            final Map<SemverKey, List<String>> versionInfo = entry.getValue();
            if (versionInfo.containsKey(SemverKey.MAJOR)) {
                sb.append(versionInfo.get(SemverKey.MAJOR).get(0));
            }

            if (versionInfo.containsKey(SemverKey.MINOR)) {
                sb.append(",");
                sb.append(versionInfo.get(SemverKey.MINOR).get(0));
            }

            if (versionInfo.containsKey(SemverKey.PATCH)) {
                sb.append(",");
                sb.append(versionInfo.get(SemverKey.PATCH).get(0));
            }

            if (versionInfo.containsKey(SemverKey.PRERELEASE)) {
                sb.append("-");
                sb.append(versionInfo.get(SemverKey.PRERELEASE).get(0));
            }

            if (versionInfo.containsKey(SemverKey.BUILD)) {
                sb.append("+");
                sb.append(versionInfo.get(SemverKey.BUILD).get(0));
            }

            flat.add(sb.toString());
        }

        Collections.sort(flat);

        return flat;
    }
}
