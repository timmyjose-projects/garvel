package com.tzj.garvel.core.builder;

import com.tzj.garvel.core.builder.api.BuildService;
import com.tzj.garvel.core.builder.api.compiler.Compiler;
import com.tzj.garvel.core.builder.api.compiler.CompilerFactory;
import com.tzj.garvel.core.builder.api.compiler.CompilerType;
import com.tzj.garvel.core.builder.api.exception.JarFileCreationException;
import com.tzj.garvel.core.builder.api.jar.JarFileCreator;
import com.tzj.garvel.core.builder.api.jar.JarFileCreatorFactory;
import com.tzj.garvel.core.builder.api.jar.JarFileCreatorOptions;
import com.tzj.garvel.core.builder.api.jar.JarFileCreatorType;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * The provider for the build builder that Garvel uses to compile the project source files.
 * There are two available options - the Tools builder and the System builder. Depending on
 * the flag in the `Garvel.gl` file, either of these compilers will be chosen (currently only
 * the Tools builder is supported).
 */
public enum BuildServiceImpl implements BuildService {
    INSTANCE;

    @Override
    public Compiler getCompiler(final CompilerType type) {
        return CompilerFactory.getCompiler(type);
    }

    @Override
    public JarFileCreator getArtifactBuilder(final JarFileCreatorType type) {
        return JarFileCreatorFactory.getJarService(type);
    }
}
