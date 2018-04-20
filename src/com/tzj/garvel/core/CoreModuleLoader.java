package com.tzj.garvel.core;

import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.core.cache.CacheManagerServiceImpl;
import com.tzj.garvel.core.cache.api.CacheManagerService;
import com.tzj.garvel.core.compiler.CompilerServiceImpl;
import com.tzj.garvel.core.compiler.api.CompilerService;
import com.tzj.garvel.core.concurrent.ConcurrencyServiceImpl;
import com.tzj.garvel.core.concurrent.api.ConcurrencyService;
import com.tzj.garvel.core.config.ConfigManagerServiceImpl;
import com.tzj.garvel.core.config.api.ConfigManagerService;
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
     * The available Java compilers in the compiler module.
     *
     * @return
     */
    public CompilerService getCompilerFramework() {
        return CompilerServiceImpl.INSTANCE;
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
}
