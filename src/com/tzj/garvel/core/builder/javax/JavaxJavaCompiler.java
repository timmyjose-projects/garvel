package com.tzj.garvel.core.builder.javax;

import com.tzj.garvel.core.builder.api.CompilationResult;
import com.tzj.garvel.core.builder.api.Compiler;
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
    private Path buildDirPath;
    private List<File> srcFiles;

    public JavaxJavaCompiler(final Path buildDirPath, final List<File> srcFiles) {
        this.buildDirPath = buildDirPath;
        this.srcFiles = srcFiles;
    }

    @Override
    public CompilationResult compile(final List<String> files) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diags = new DiagnosticCollector<>();
        StandardJavaFileManager manager = compiler.getStandardFileManager(diags, Locale.getDefault(), Charset.forName("UTF-8"));

        Iterable<? extends JavaFileObject> units = manager.getJavaFileObjectsFromFiles(srcFiles);
        List<String> compilationOptions = getCompilationOptions();
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
        }

        return compilationResult;
    }

    /**
     * These options can be read from the config file in future versions.
     * Currently, these are hardcoded in.
     *
     * @return
     */
    private List<String> getCompilationOptions() {
        return Arrays.asList(
                CompilationOption.XLINT.toString(),
                CompilationOption.TARGET_DIR.toString(),
                String.format(buildDirPath.toFile().getAbsolutePath()),
                CompilationOption.CLASSPATH.toString(),
                String.format("%s%s%s", ".", File.pathSeparator, "java.class.path")
        );
    }
}
