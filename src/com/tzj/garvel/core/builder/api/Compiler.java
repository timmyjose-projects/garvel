package com.tzj.garvel.core.builder.api;

import java.util.List;

public interface Compiler {
    CompilationResult compile(List<String> files);
}
