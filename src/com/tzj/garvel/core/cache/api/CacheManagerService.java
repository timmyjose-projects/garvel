package com.tzj.garvel.core.cache.api;

import com.tzj.garvel.core.cache.exception.CacheManagerException;

public interface CacheManagerService {
    void populateCoreCaches() throws CacheManagerException;

    boolean isConfigCachePopulated();

    boolean isLockCachePopulated();

    DependenciesEntry getConfigDependencies();

    DependenciesEntry getLockDependencies();
}
