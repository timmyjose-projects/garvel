package com.tzj.garvel.core.cache;

import com.tzj.garvel.core.cache.api.CacheManagerService;

/**
 * An LRU cache that will be used to cache changes to the Garvel.gl file,
 * as well as cache previously compiled code amongst other configuration
 * details.
 * This will come in handy if Garvel core will be deployed as service
 *
 */
public enum CacheManagerServiceImpl implements CacheManagerService {
    INSTANCE;
}
