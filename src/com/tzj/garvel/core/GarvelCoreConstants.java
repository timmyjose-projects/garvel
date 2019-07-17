package com.tzj.garvel.core;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Most of the contents of this file should be placed in an external
 * configuration file that may or may not be affected by builds.
 */
public class GarvelCoreConstants {
    // Garvel config
    public static final String GARVEL_HOME_DIR = System.getProperty("user.home");

    public static final String GARVEL_DIR = GARVEL_HOME_DIR + File.separator + ".garvel";

    public static final String GARVEL_CACHE_DIR = GARVEL_DIR + File.separator + "cache";

    public static final String GARVEL_CACHE_FILE = GARVEL_CACHE_DIR +
            File.separator + "cache.mapping";

    public static final String POSIX_PERMISSIONS = "rwxr-xr-x";

    // Garvel codebase specific
    public static final String GARVEL_CONFIG_TEMPLATE = "com/tzj/garvel/common/templates/GarvelTemplate.gl";

    // Garvel project config
    public static final String GARVEL_PROJECT_ROOT = System.getProperty("user.dir");

    // all packages under this root are considered part of the source tree
    public static final String GARVEL_PROJECT_SOURCE_ROOT = GARVEL_PROJECT_ROOT +
            File.separator + "src";

    public static final String GARVEl_PROJECT_CONFIG_FILE = GARVEL_PROJECT_ROOT +
            File.separator + "Garvel.gl";

    public static final String GARVEL_PROJECT_LOCK_FILE = GARVEL_PROJECT_ROOT +
            File.separator + "Garvel.lock";

    public static final String GARVEL_PROJECT_TARGET_DIR = GARVEL_PROJECT_ROOT
            + File.separator + "target";

    public static final String GARVEL_PROJECT_BUILD_DIR = GARVEL_PROJECT_TARGET_DIR +
            File.separator + "build";

    public static final String GARVEL_PROJECT_DEPS_DIR = GARVEL_PROJECT_TARGET_DIR +
            File.separator + "deps";

    public static final String GARVEL_PROJECT_DEPS_FILE = GARVEL_PROJECT_DEPS_DIR +
            File.separator + "dependency.graph";


    // this should be moved to an external file that can be auto-incremented
    // with each build (post bootstrap of garvel to garvel)
    // Note that this provides Garvel's own version, not the project
    // version, which should be queried from the cache, and which
    // should be that specified in the Garvel.gl file.
    public static final String GARVEL_GARVEL_VERSION = "1.1.0";

    // this should be moved to an external file that can be auto-incremented
    public static final List<String> installedCommands = Arrays.asList(
            "help",
            "list",
            "version",
            "install",
            "uninstall",
            "init",
            "new",
            "build",
            "clean",
            "run",
            "update",
            "dep",
            "test"
    );
}
