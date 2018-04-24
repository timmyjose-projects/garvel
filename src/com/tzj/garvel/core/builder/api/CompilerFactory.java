package com.tzj.garvel.core.builder.api;

import com.tzj.garvel.core.builder.javax.JavaxJavaCompiler;
import com.tzj.garvel.core.builder.system.SystemJavaCompiler;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class CompilerFactory {
    private CompilerFactory() {
    }

    public static Compiler getCompiler(CompilerType type, final Path buildDirPath, final List<File> srcFiles) {
        Compiler compiler = null;

        switch (type) {
            case JAVAX_JAVACOMPILER:
                compiler = new JavaxJavaCompiler(buildDirPath, srcFiles);
                break;
            case SYSTEM_JAVACOMPILER:
                compiler = new SystemJavaCompiler(buildDirPath, srcFiles);
                break;
        }

        return compiler;
    }
}
