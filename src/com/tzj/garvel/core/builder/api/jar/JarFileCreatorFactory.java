package com.tzj.garvel.core.builder.api.jar;

import com.tzj.garvel.core.builder.jar.FatJarCreator;
import com.tzj.garvel.core.builder.jar.NormalJarFileCreator;

import java.nio.file.Path;

/**
 * factory for the kind of artifact that is desired.
 */
public class JarFileCreatorFactory {
    private JarFileCreatorFactory() {
    }

    public static JarFileCreator getJarService(JarFileCreatorType type) {
        JarFileCreator jarService = null;

        switch (type) {
            case NORMAL_JAR:
                jarService = new NormalJarFileCreator();
                break;
            case FAT_JAR:
                jarService = new FatJarCreator();
                break;
        }

        return jarService;
    }
}
