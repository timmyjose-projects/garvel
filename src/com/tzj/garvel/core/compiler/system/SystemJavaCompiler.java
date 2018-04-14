package com.tzj.garvel.core.compiler.system;

import com.tzj.garvel.core.compiler.api.CompilationResult;
import com.tzj.garvel.core.compiler.api.Compiler;

import java.util.List;

public enum SystemJavaCompiler implements Compiler {
    INSTANCE;

    @Override
    public CompilationResult compile(final List<String> files) {
        return null;
    }
}
