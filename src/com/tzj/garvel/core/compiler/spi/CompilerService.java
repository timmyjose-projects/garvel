package com.tzj.garvel.core.compiler.spi;

public interface CompilerService {
    Compiler getCompiler(CompilerType type);
}
