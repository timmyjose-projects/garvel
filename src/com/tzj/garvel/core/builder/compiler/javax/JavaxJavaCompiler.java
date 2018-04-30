package com.tzj.garvel.core.builder.compiler.javax;

import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.builder.api.CompilationResult;
import com.tzj.garvel.core.builder.api.compiler.Compiler;
import com.tzj.garvel.core.builder.api.exception.CompilationException;

import javax.lang.model.SourceVersion;
import javax.tools.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

/**
 * This uses the Java builder provided by the JDK Tools library.
 */
public class JavaxJavaCompiler implements Compiler {
    private static final String TOOL_PROVIDER = "javax.tools.ToolProvider";
    private static final String RELEASE_STRING = "RELEASE_";

    /**
     * Compile sources (supplied as a list of File objects) into the supplied
     * directory.
     *
     * @return
     */
    @Override
    public CompilationResult compile(Path buildDirPath, List<File> srcFiles, List<String> compilationOptions) throws CompilationException {
        // setup
        final JavaCompiler compiler = validateCompiler();
        final DiagnosticCollector<JavaFileObject> diags = new DiagnosticCollector<>();
        final StandardJavaFileManager manager = compiler.getStandardFileManager(diags, Locale.getDefault(), Charset.forName("UTF-8"));

        final Iterable<? extends JavaFileObject> units = manager.getJavaFileObjectsFromFiles(srcFiles);
        final JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diags, compilationOptions, null, units);

        // invoke
        task.call();

        final CompilationResult compilationResult = new CompilationResult();

        // report any compilation errors.
        if (diags.getDiagnostics().size() != 0) {
            compilationResult.setSuccessful(false);
            List<String> diagMessages = new ArrayList<>(diags.getDiagnostics().size());

            for (Diagnostic<? extends JavaFileObject> d : diags.getDiagnostics()) {
                diagMessages.add(String.format("%s:%d: %s\n",
                        d.getSource().getName(),
                        d.getLineNumber(),
                        d.getMessage(Locale.getDefault())));
            }
            compilationResult.setDiagnostics(diagMessages);
        } else {
            compilationResult.setSuccessful(true);
        }

        return compilationResult;
    }

    /**
     * This will check if the user's system actually has a JDK installed.
     * The basic idea is that if the javax.tools.ToolProvider class is available,
     * and the returned Java compiler is not null,
     * then the user's system does have a JDK installed.
     */
    private JavaCompiler validateCompiler() throws CompilationException {
        try {
            Class.forName(TOOL_PROVIDER);
        } catch (ClassNotFoundException e) {
            throw new CompilationException("failed to detect JDK");
        }

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        if (compiler == null) {
            throw new CompilationException("failed to detect JDK");
        }

        final SourceVersion latestSupportedVersion = SourceVersion.latestSupported();

        if (latestSupportedVersion == null) {
            throw new CompilationException("failed to detect JDK");
        }

        final String jdkVersion = latestSupportedVersion.toString().replace(RELEASE_STRING, "");

        UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "Detected JDK version %s", jdkVersion);
        UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "Starting compilation of source files...");

        return compiler;
    }
}
