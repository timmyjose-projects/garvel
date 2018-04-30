package com.tzj.garvel.core;

import com.tzj.garvel.core.builder.BuildServiceImpl;
import com.tzj.garvel.core.builder.api.BuildService;
import com.tzj.garvel.core.cache.CacheManagerServiceImpl;
import com.tzj.garvel.core.cache.api.CacheManagerService;
import com.tzj.garvel.core.concurrent.ConcurrencyServiceImpl;
import com.tzj.garvel.core.concurrent.api.ConcurrencyService;
import com.tzj.garvel.core.config.ConfigManagerServiceImpl;
import com.tzj.garvel.core.config.api.ConfigManagerService;
import com.tzj.garvel.core.dep.DependencyManagerServiceImpl;
import com.tzj.garvel.core.dep.api.DependencyManagerService;
import com.tzj.garvel.core.filesystem.FilesystemServiceImpl;
import com.tzj.garvel.core.filesystem.api.FilesystemService;
import com.tzj.garvel.core.net.NetworkServiceImpl;
import com.tzj.garvel.core.net.api.NetworkService;
import com.tzj.garvel.core.parser.ParserServiceImpl;
import com.tzj.garvel.core.parser.api.ParserService;


public enum CoreModuleLoader {
    INSTANCE;

    /**
     * Generic concurrency framework.
     *
     * @return
     */
    public ConcurrencyService getConcurrencyFramework() {
        return ConcurrencyServiceImpl.INSTANCE;
    }

    /**
     * The available parsers in the parser module.
     *
     * @return
     */
    public ParserService getParserFramework() {
        return ParserServiceImpl.INSTANCE;
    }

    /**
     * The available Java compilers and JAR file
     * reators in the builder module.
     *
     * @return
     */
    public BuildService getCompilerFramework() {
        return BuildServiceImpl.INSTANCE;
    }

    /**
     * Handler for all file system related activities.
     *
     * @return
     */
    public FilesystemService getFileSystemFramework() {
        return FilesystemServiceImpl.INSTANCE;
    }

    /**
     * Handler for all network communication.
     *
     * @return
     */
    public NetworkService getNetworkFramework() {
        return NetworkServiceImpl.INSTANCE;
    }

    /**
     * Handler for all configuration-related functionality.
     *
     * @return
     */
    public ConfigManagerService getConfigManager() {
        return ConfigManagerServiceImpl.INSTANCE;
    }

    /**
     * Return the Cache Manager that holds the parsed configuration data.
     *
     * @return
     */
    public CacheManagerService getCacheManager() {
        return CacheManagerServiceImpl.INSTANCE;
    }

    /**
     * The main module - handles all dependency management related functionality.
     */
    public DependencyManagerService getDependencyManager() {
        return DependencyManagerServiceImpl.INSTANCE;
    }
}
