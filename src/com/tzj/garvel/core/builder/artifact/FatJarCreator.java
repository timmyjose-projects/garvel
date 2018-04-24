package com.tzj.garvel.core.builder.artifact;

import com.tzj.garvel.core.builder.api.JarFileCreatorService;
import com.tzj.garvel.core.builder.api.JarFileFields;
import com.tzj.garvel.core.builder.exception.JarFileCreationException;

import java.nio.file.Path;

public class FatJarCreator implements JarFileCreatorService {
    private final Path buildDirPath;
    private final JarFileFields fields;

    public FatJarCreator(final Path buildDirPath, final JarFileFields fields) {
        this.buildDirPath = buildDirPath;
        this.fields = fields;
    }

    @Override
    public Path createJarFile() throws JarFileCreationException {
        return null;
    }
}
