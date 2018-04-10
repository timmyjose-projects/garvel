package com.tzj.garvel.playground.javacompiler;

import javax.tools.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BasicCompileDemo {
    private static final String BASE = "/Users/z0ltan/Code/Projects/Playground/dummy/com/foo/bar/";

    public static void main(String[] args) {
        List<String> filePaths = Arrays.asList(BASE + "HelloWorld.java");

        List<File> files = new ArrayList<>();

        for (String path : filePaths) {
            files.add(Paths.get(path).toFile());
        }

        compileFiles(files);
    }

    private static void compileFiles(List<File> files) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.getDefault(), Charset.forName("UTF-8"));

        Iterable<? extends JavaFileObject> units = fileManager.getJavaFileObjectsFromFiles(files);
        compiler.getTask(null, fileManager, diagnostics, null, null, units).call();

        for (Diagnostic<? extends JavaFileObject> d : diagnostics.getDiagnostics()) {
            System.out.format("%s:%d:%s\n", d.getSource().getName(), d.getLineNumber(), d.getMessage(Locale.getDefault()));
        }
    }
}
