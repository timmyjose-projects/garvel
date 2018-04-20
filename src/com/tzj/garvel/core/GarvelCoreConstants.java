package com.tzj.garvel.core;

import java.util.Arrays;
import java.util.List;

/**
 * Most of the contents of this file should be placed in an external
 * configuration file that may or may not be affected by builds.
 */
public class GarvelCoreConstants {
    public static final String GARVEL_HOME_DIR = System.getProperty("user.home");

    public static final String GARVEL_DIR = ".garvel";

    public static final String POSIX_PERMISSIONS = "rwxr-xr-x";

    public static final String GARVEL_CONFIG_FILE = "Garvel.gl";

    public static final String GARVEL_LOCK_FILE = "Garvel.lock";

    // this should be moved to an external file that can be auto-incremented
    // with each build (post bootstrap of garvel to garvel)
    public static final String GARVEL_VERSION = "0.0.1";

    // this should be moved to an external file that can be auto-incremented
    public static final List<String> installedCommands = Arrays.asList("help", "list", "version", "new", "init", "build", "clean", "run", "test");
}
