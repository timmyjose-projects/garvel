package com.tzj.garvel.core.cache.api;

import com.tzj.garvel.core.cache.exception.CacheManagerException;

public interface CacheManagerService {
    CacheManagerService populateCache() throws CacheManagerException;

}
