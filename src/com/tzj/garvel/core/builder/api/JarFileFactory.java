package com.tzj.garvel.core.builder.api;

import com.tzj.garvel.core.builder.artifact.FatJarCreatorService;
import com.tzj.garvel.core.builder.artifact.NormalJarFileCreator;

public class JarFileFactory {
    private JarFileFactory() {
    }

    public static JarFileCreatorService getJarService(JarFileType type) {
        JarFileCreatorService jarService = null;

        switch (type) {
            case NORMAL_JAR:
                jarService = new NormalJarFileCreator();
                break;
            case FAT_JAR:
                jarService = new FatJarCreatorService();
                break;
        }

        return jarService;
    }
}
