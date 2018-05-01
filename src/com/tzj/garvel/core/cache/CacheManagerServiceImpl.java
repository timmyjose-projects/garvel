package com.tzj.garvel.core.cache;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.cache.api.*;
import com.tzj.garvel.core.cache.exception.CacheManagerException;
import com.tzj.garvel.core.parser.api.ast.toml.ConfigAst;
import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;
import com.tzj.garvel.core.parser.exception.TOMLParserException;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.tzj.garvel.common.parser.GarvelConstants.DASH;
import static com.tzj.garvel.core.dep.api.repo.RepositoryConstants.JAR;

/**
 * Cache changes to the Garvel.gl file, Right now it doesn't make any
 * difference, but if `core` were to become a service in a future iteration,
 * it would help improve performance tremendously.
 */
public enum CacheManagerServiceImpl implements CacheManagerService {
    INSTANCE;

    private Map<CacheKey, CacheEntry> configCache; // Garvel.gl
    private Map<CacheKey, CacheEntry> lockCache; // Garvel.lock, if present

    private CacheManagerServiceImpl() {
        if (configCache == null) {
            try {
                populateCoreCaches();
            } catch (CacheManagerException e) {
                //
            }
        }
    }

    /**
     * Populate the Core Cache with the data from the Garvel.gl configuration file.
     *
     * @throws CacheManagerException
     */
    @Override
    public void populateCoreCaches() throws CacheManagerException {
        // required
        if (!CoreModuleLoader.INSTANCE.getFileSystemFramework().checkFileExists(GarvelCoreConstants.GARVEl_PROJECT_CONFIG_FILE)) {
            throw new CacheManagerException(String.format("Garvel configuration file %s does not exist!", GarvelCoreConstants.GARVEl_PROJECT_CONFIG_FILE));
        }

        configCache = new HashMap<>();

        try {
            final ConfigAst config = CoreModuleLoader.INSTANCE
                    .getParserFramework()
                    .getTOMLParser(GarvelCoreConstants.GARVEl_PROJECT_CONFIG_FILE)
                    .parse();

            populateConfigCache(config);
        } catch (TOMLParserException e) {
            throw new CacheManagerException(String.format("Error while populating Garvel config cache. Reason = %s", e.getErrorString()));
        }

        // optional
        if (CoreModuleLoader.INSTANCE.getFileSystemFramework().checkFileExists(GarvelCoreConstants.GARVEL_PROJECT_LOCK_FILE)) {
            try {
                final ConfigAst lock = CoreModuleLoader.INSTANCE
                        .getParserFramework()
                        .getTOMLParser(GarvelCoreConstants.GARVEL_PROJECT_LOCK_FILE)
                        .parse();

                populateLockCache(lock);
            } catch (TOMLParserException e) {
                throw new CacheManagerException(String.format("Error while populating Garvel lock cache. Reason = %s", e.getErrorString()));
            }
        }
    }

    /**
     * Check if the config cache has been populated using Garvel.gl.
     *
     * @return
     */
    @Override
    public boolean isConfigCachePopulated() {
        return configCache != null;
    }

    /**
     * Check if the lock cache has been populate using Garvel.lock.
     *
     * @return
     */
    @Override
    public boolean isLockCachePopulated() {
        return lockCache != null;
    }

    @Override
    public DependenciesEntry getConfigDependencies() {
        if (configCache != null) {
            return (DependenciesEntry) configCache.get(CacheKey.DEPENDENCIES);
        }

        return null;
    }

    @Override
    public DependenciesEntry getLockDependencies() {
        if (lockCache != null) {
            return (DependenciesEntry) lockCache.get(CacheKey.DEPENDENCIES);
        }

        return null;
    }

    /**
     * Some of the keys are optional, and so this method needs to be
     * invoked before those operations.
     *
     * @param key
     * @return
     */
    @Override
    public boolean containsCacheKey(final CacheKey key) {
        return configCache.containsKey(key);
    }

    /**
     * Given the key, return the corresponding cache entry, or null if
     * not available.
     *
     * @param key
     * @return
     * @throws CacheManagerException
     */
    @Override
    public CacheEntry getEntry(final CacheKey key) {
        if (configCache.containsKey(key)) {
            return configCache.get(key);
        }

        return null;
    }

    /**
     * Populate the config cache.
     *
     * @param config
     */
    private void populateConfigCache(final ConfigAst config) {
        configCache = new HashMap<>();

        TOMLAstVisitor configCacheFillVisitor = new TOMLAstCacheVisitor(configCache);
        config.accept(configCacheFillVisitor);
    }

    /**
     * Populate the lock cache, if applicable.
     *
     * @param lock
     */
    private void populateLockCache(final ConfigAst lock) {
        lockCache = new HashMap<>();

        TOMLAstVisitor lockCacheFillVisitor = new TOMLAstCacheVisitor(lockCache);
        lock.accept(lockCacheFillVisitor);
    }
}
