package com.tzj.garvel.core.builder.api;

import com.tzj.garvel.core.builder.exception.JarFileCreationException;

import java.nio.file.Path;

public interface JarFileCreatorService {
    Path createJarFile() throws JarFileCreationException;
}
