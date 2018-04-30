package com.tzj.garvel.core.builder.api.compiler;

import com.tzj.garvel.core.builder.compiler.javax.JavaxJavaCompiler;
import com.tzj.garvel.core.builder.compiler.system.SystemJavaCompiler;

/**
 * Factory for the Java compiler to use for building the project. Currently, this only
 * supports the javax.tools Java Compiler.
 */
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
