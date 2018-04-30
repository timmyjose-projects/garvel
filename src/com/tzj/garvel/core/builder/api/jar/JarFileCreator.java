package com.tzj.garvel.core.builder.api.jar;

import com.tzj.garvel.core.builder.api.exception.JarFileCreationException;

import java.nio.file.Path;

public interface JarFileCreator {
    Path createJarFile(final Path buildDirPath, final JarFileCreatorOptions options) throws JarFileCreationException;
}
