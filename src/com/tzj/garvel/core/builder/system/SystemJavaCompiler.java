package com.tzj.garvel.core.builder.system;

import com.tzj.garvel.core.builder.api.CompilationResult;
import com.tzj.garvel.core.builder.api.Compiler;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class SystemJavaCompiler implements Compiler {
    private final Path buildDirPath;
    private final List<File> srcFiles;

    public SystemJavaCompiler(final Path buildDirPath, final List<File> srcFiles) {
        this.buildDirPath = buildDirPath;
        this.srcFiles = srcFiles;
    }

    @Override
    public CompilationResult compile(final List<String> files) {
        return null;
    }
}
