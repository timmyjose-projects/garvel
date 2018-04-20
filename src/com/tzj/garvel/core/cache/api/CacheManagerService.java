package com.tzj.garvel.core.cache.api;

import com.tzj.garvel.core.cache.exception.CacheManagerException;

public interface CacheManagerService {
    void populateCache() throws CacheManagerException;

}
