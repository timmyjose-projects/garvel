package com.tzj.garvel.core.compiler;

import com.tzj.garvel.core.compiler.spi.Compiler;
import com.tzj.garvel.core.compiler.spi.CompilerFactory;
import com.tzj.garvel.core.compiler.spi.CompilerService;
import com.tzj.garvel.core.compiler.spi.CompilerType;

public class CompilerServiceImpl implements CompilerService {
    @Override
    public Compiler getCompiler(final CompilerType type) {
        return CompilerFactory.getCompiler(type);
    }
}
