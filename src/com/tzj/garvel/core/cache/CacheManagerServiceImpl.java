package com.tzj.garvel.core.cache;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.cache.api.CacheEntry;
import com.tzj.garvel.core.cache.api.CacheMainKey;
import com.tzj.garvel.core.cache.api.CacheManagerService;
import com.tzj.garvel.core.cache.exception.CacheManagerException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An LRU cache that will be used to cache changes to the Garvel.gl file,
 * as well as cache previously compiled code amongst other configuration
 * details.
 * This will come in handy if Garvel core will be deployed as service
 */
public enum CacheManagerServiceImpl implements CacheManagerService {
    INSTANCE;

    private Map<CacheMainKey, List<CacheEntry>> configCache;
    private Map<String, String> dependenciesCache;

    @Override
    public void populateCache() throws CacheManagerException {
        configCache = new HashMap<>();
        dependenciesCache = new HashMap<>();

    }
}
