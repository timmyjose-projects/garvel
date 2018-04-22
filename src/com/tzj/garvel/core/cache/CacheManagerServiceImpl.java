package com.tzj.garvel.core.cache;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.cache.api.CacheEntry;
import com.tzj.garvel.core.cache.api.CacheKey;
import com.tzj.garvel.core.cache.api.CacheManagerService;
import com.tzj.garvel.core.cache.exception.CacheManagerException;
import com.tzj.garvel.core.parser.api.ast.toml.ConfigAst;
import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;
import com.tzj.garvel.core.parser.exception.TOMLParserException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache changes to the Garvel.gl file, Right now it doesn't make any
 * difference, but if `core` were to become a service in a future iteration,
 * it would help improve performance tremendously.
 */
public enum CacheManagerServiceImpl implements CacheManagerService {
    INSTANCE;

    private Map<CacheKey, CacheEntry> configCache;

    @Override
    public CacheManagerService populateCache() throws CacheManagerException {
        configCache = new HashMap<>();

        try {
            final String garvelConfigFile = GarvelCoreConstants.GARVEL_PROJECT_ROOT
                    + File.separator
                    + GarvelCoreConstants.GARVEL_CONFIG_FILE;

            final ConfigAst config = CoreModuleLoader.INSTANCE
                    .getParserFramework()
                    .getTOMLParser(garvelConfigFile)
                    .parse();

            populateConfigCache(config);
        } catch (TOMLParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void populateConfigCache(final ConfigAst config) {
        configCache = new HashMap<>();

        TOMLAstVisitor cacheFillVisitor = new TOMLAstCacheVisitor(configCache);
        config.accept(cacheFillVisitor);
    }
}
