package com.tzj.garvel.core.compiler.spi;

import java.util.List;

public interface Compiler {
    CompilationResult compile(List<String> files);
}
