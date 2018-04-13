package com.tzj.garvel.core.compiler.spi;

import com.tzj.garvel.core.compiler.javax.JavaxJavaCompiler;
import com.tzj.garvel.core.compiler.system.SystemJavaCompiler;

public class CompilerFactory {
    private CompilerFactory() {
    }

    public static Compiler getCompiler(CompilerType type) {
        Compiler compiler = null;

        switch (type) {
            case JAVAX_JAVACOMPILER:
                compiler = JavaxJavaCompiler.INSTANCE;
                break;
            case SYSTEM_JAVACOMPILER:
                compiler = SystemJavaCompiler.INSTANCE;
                break;
        }

        return compiler;
    }
}
