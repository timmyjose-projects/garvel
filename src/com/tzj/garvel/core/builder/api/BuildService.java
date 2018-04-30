package com.tzj.garvel.core.builder.api;

import com.tzj.garvel.core.builder.api.compiler.Compiler;
import com.tzj.garvel.core.builder.api.compiler.CompilerType;
import com.tzj.garvel.core.builder.api.jar.JarFileCreator;
import com.tzj.garvel.core.builder.api.jar.JarFileCreatorType;

public interface BuildService {
    Compiler getCompiler(final CompilerType type);

    JarFileCreator getArtifactBuilder(final JarFileCreatorType type);
}
