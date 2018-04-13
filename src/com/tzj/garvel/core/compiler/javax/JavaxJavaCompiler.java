package com.tzj.garvel.core.compiler.javax;

import com.tzj.garvel.core.compiler.spi.CompilationResult;
import com.tzj.garvel.core.compiler.spi.Compiler;

import java.util.List;

public enum JavaxJavaCompiler implements Compiler {
    INSTANCE;

    @Override
    public CompilationResult compile(final List<String> files) {
        return null;
    }
}
