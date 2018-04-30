package com.tzj.garvel.core.builder.jar;

import com.tzj.garvel.core.builder.api.jar.JarFileCreator;
import com.tzj.garvel.core.builder.api.exception.JarFileCreationException;
import com.tzj.garvel.core.builder.api.jar.JarFileCreatorOptions;

import java.nio.file.Path;

public class FatJarCreator implements JarFileCreator {
    @Override
    public Path createJarFile(final Path buildDirPath, final JarFileCreatorOptions options) throws JarFileCreationException {
        return null;
    }
}
