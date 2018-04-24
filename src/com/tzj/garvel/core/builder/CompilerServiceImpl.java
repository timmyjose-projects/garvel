package com.tzj.garvel.core.builder;

import com.tzj.garvel.core.builder.api.Compiler;
import com.tzj.garvel.core.builder.api.CompilerFactory;
import com.tzj.garvel.core.builder.api.CompilerService;
import com.tzj.garvel.core.builder.api.CompilerType;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * The provider for the build builder that Garvel uses to compile the project source files.
 * There are two available options - the Tools builder and the System builder. Depending on
 * the flag in the `Garvel.gl` file, either of these compilers will be chosen (currently only
 * the Tools builder is supported).
 */
public enum CompilerServiceImpl implements CompilerService {
    INSTANCE;

    @Override
    public Compiler getCompiler(final CompilerType type, final Path buildDirPath, final List<File> srcFiles) {
        return CompilerFactory.getCompiler(type, buildDirPath, srcFiles);
    }
}
