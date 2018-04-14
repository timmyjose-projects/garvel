package com.tzj.garvel.core.compiler.api;

import java.util.List;

public interface Compiler {
    CompilationResult compile(List<String> files);
}
