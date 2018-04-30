package com.tzj.garvel.core.builder.compiler.system;

import com.tzj.garvel.core.builder.api.CompilationResult;
import com.tzj.garvel.core.builder.api.compiler.Compiler;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class SystemJavaCompiler implements Compiler {
    @Override
    public CompilationResult compile(final Path buildDirPath, final List<File> srcFiles, final List<String> compilationOptions) {
        return null;
    }
}
