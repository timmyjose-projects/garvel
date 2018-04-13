package com.tzj.garvel.core;

import com.tzj.garvel.core.compiler.spi.CompilerService;
import com.tzj.garvel.core.concurrent.spi.ConcurrencyService;
import com.tzj.garvel.core.filesystem.spi.FilesystemService;
import com.tzj.garvel.core.net.spi.NetworkService;
import com.tzj.garvel.core.parser.spi.ParserService;

import java.util.ServiceLoader;

public enum CoreModuleLoader {
    INSTANCE;


    private static <T> T loadFirstNonNull(Class<T> clazz, final String moduleName) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz);

        T service = null;
        for (T t : loader) {
            if (t != null) {
                service = t;
                break;
            }
        }

        if (service == null) {
            throw new IllegalStateException("Unable to load " + moduleName);
        }

        return service;
    }


    /**
     * Generic concurrency framework.
     *
     * @return
     */
    public ConcurrencyService getConcurrencyFramework() {
        return loadFirstNonNull(ConcurrencyService.class, "Concurrency Framework");
    }

    /**
     * The available parsers in the parser module.
     *
     * @return
     */
    public ParserService getParserFramework() {
        return loadFirstNonNull(ParserService.class, "Parsing Framework");
    }

    /**
     * The available Java compilers in the compiler module.
     *
     * @return
     */
    public CompilerService getCompilerFramework() {
        return loadFirstNonNull(CompilerService.class, "Compiler Framework");
    }

    /**
     * Handler for all file system related activities.
     *
     * @return
     */
    public FilesystemService getFileSystemFramework() {
        return loadFirstNonNull(FilesystemService.class, "File System Framework");
    }

    /**
     * Handler for all network communication.
     *
     * @return
     */
    public NetworkService getNetworkFramework() {
        return loadFirstNonNull(NetworkService.class, "Network Framework");
    }
}
