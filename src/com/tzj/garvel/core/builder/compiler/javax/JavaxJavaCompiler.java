package com.tzj.garvel.core.builder.compiler.javax;

import com.tzj.garvel.core.builder.api.CompilationResult;
import com.tzj.garvel.core.builder.api.compiler.Compiler;
import com.tzj.garvel.core.builder.common.CompilationOption;

import javax.tools.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

/**
 * This uses the Java builder provided by the JDK Tools library.
 */
public class JavaxJavaCompiler implements Compiler {
    /**
     * Compile sources (supplied as a list of File objects) into the supplied
     * directory.
     *
     * @return
     */
    @Override
    public CompilationResult compile(Path buildDirPath, List<File> srcFiles, List<String> compilationOptions) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diags = new DiagnosticCollector<>();
        StandardJavaFileManager manager = compiler.getStandardFileManager(diags, Locale.getDefault(), Charset.forName("UTF-8"));

        Iterable<? extends JavaFileObject> units = manager.getJavaFileObjectsFromFiles(srcFiles);
        final JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diags, compilationOptions, null, units);
        task.call();

        final CompilationResult compilationResult = new CompilationResult();

        // report any compilation errors.
        if (diags.getDiagnostics().size() != 0) {
            compilationResult.setSuccessful(false);
            List<String> diagMessages = new ArrayList<>(diags.getDiagnostics().size());

            for (Diagnostic<? extends JavaFileObject> d : diags.getDiagnostics()) {
                diagMessages.add(String.format("%s:%d:%s\n", d.getSource().getName(), d.getLineNumber(), d.getSource().toUri()));
            }
            compilationResult.setDiagnostics(diagMessages);
        } else {
            compilationResult.setSuccessful(true);
        }

        return compilationResult;
    }
}
