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

    public static final String GARVEL_DIR = ".garvel";

    public static final String GARVEL_CACHE_DIR = "cache";

    public static final String GARVEL_CACHE_FILE = GARVEL_CACHE_DIR +
            File.separator + "cache.mapping";

    public static final String POSIX_PERMISSIONS = "rwxr-xr-x";

    // Garvel codebase specific
    public static final String GARVEL_CONFIG_TEMPLATE = "com/tzj/garvel/common/templates/GarvelTemplate.gl";

    // Garvel project config
    public static final String GARVEL_PROJECT_ROOT = System.getProperty("user.dir");

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
    public static final String GARVEL_VERSION = "0.0.1";

    // this should be moved to an external file that can be auto-incremented
    public static final List<String> installedCommands = Arrays.asList(
            "help",
            "list",
            "version",
            "install",
            "uninstall",
            "new",
            "build",
            "clean",
            "run",
            "update",
            "dep",
            "test"
    );
}
