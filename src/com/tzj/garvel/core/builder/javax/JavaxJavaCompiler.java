package com.tzj.garvel.core.builder.javax;

import com.tzj.garvel.core.builder.api.CompilationResult;
import com.tzj.garvel.core.builder.api.Compiler;

import java.io.File;
import java.util.*;

/**
 * This uses the Java builder provided by the JDK Tools library.
 */
public class JavaxJavaCompiler implements Compiler {
    @Override
    public CompilationResult compile(final List<String> files) {
        List<File> sourceFiles = new ArrayList<>();
        Set<String> compilationOptions = new HashSet<>();
        return null;
    }
}
