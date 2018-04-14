package com.tzj.garvel.core.compiler;

import com.tzj.garvel.core.compiler.api.Compiler;
import com.tzj.garvel.core.compiler.api.CompilerFactory;
import com.tzj.garvel.core.compiler.api.CompilerService;
import com.tzj.garvel.core.compiler.api.CompilerType;

public enum CompilerServiceImpl implements CompilerService {
    INSTANCE;
    
    @Override
    public Compiler getCompiler(final CompilerType type) {
        return CompilerFactory.getCompiler(type);
    }
}
