package com.tzj.garvel.core.builder.api;

import com.tzj.garvel.core.builder.javax.JavaxJavaCompiler;
import com.tzj.garvel.core.builder.system.SystemJavaCompiler;

public class CompilerFactory {
    private CompilerFactory() {
    }

    public static Compiler getCompiler(CompilerType type) {
        Compiler compiler = null;

        switch (type) {
            case JAVAX_JAVACOMPILER:
                compiler = new JavaxJavaCompiler();
                break;
            case SYSTEM_JAVACOMPILER:
                compiler = new SystemJavaCompiler();
                break;
        }

        return compiler;
    }
}
