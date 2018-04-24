package com.tzj.garvel.core.builder.api;

import com.tzj.garvel.core.builder.artifact.FatJarCreator;
import com.tzj.garvel.core.builder.artifact.NormalJarFileCreator;

import java.nio.file.Path;

public class JarFileFactory {
    private JarFileFactory() {
    }

    public static JarFileCreatorService getJarService(JarFileType type, final Path buildDirPath, final JarFileFields fields) {
        JarFileCreatorService jarService = null;

        switch (type) {
            case NORMAL_JAR:
                jarService = new NormalJarFileCreator(buildDirPath, fields);
                break;
            case FAT_JAR:
                jarService = new FatJarCreator(buildDirPath, fields);
                break;
        }

        return jarService;
    }
}
