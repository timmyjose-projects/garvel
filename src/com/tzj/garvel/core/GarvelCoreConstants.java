package com.tzj.garvel.core;

import java.util.Arrays;
import java.util.List;

public class GarvelCoreConstants {
    public static final String GARVEL_DIR = ".garvel";

    // this should be moved to an external file that can be auto-incremented
    // with each build (post bootstrap of garvel to garvel)
    public static final String GARVEL_VERSION = "0.0.1";

    // this should be moved to an external file that can be auto-incremented
    public static final List<String> installedCommands = Arrays.asList("help", "list", "version", "new", "init", "build", "clean", "run", "test");
}
