package com.tzj.garvel.core.compiler.system;

import com.tzj.garvel.core.compiler.spi.CompilationResult;
import com.tzj.garvel.core.compiler.spi.Compiler;

import java.util.List;

public enum SystemJavaCompiler implements Compiler {
    INSTANCE;

    @Override
    public CompilationResult compile(final List<String> files) {
        return null;
    }
}
