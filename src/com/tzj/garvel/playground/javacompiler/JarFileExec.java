package com.tzj.garvel.playground.javacompiler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;
import java.util.jar.JarFile;

public class JarFileExec {
    private static final String JAR_FILE = "/Users/z0ltan/Code/Projects/Playground/testbed/foo/target/foo-1.2.3.jar";

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        final String className = in.nextLine().trim();

        final URL[] urls = new URL[]{new URL("jar:file:" + JAR_FILE + "!/")};
        final URLClassLoader loader = new URLClassLoader(urls);

        Class<?> clazz = null;
        try {
            clazz = loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        runMain(clazz, args);
    }

    private static void runMain(Class<?> clazz, final String[] args) {
        try {
            final Method mainMethod = clazz.getMethod("main", String[].class);
            final int mods = mainMethod.getModifiers();

            mainMethod.setAccessible(true);
            if (!Modifier.isPublic(mods) || !Modifier.isStatic(mods) || !mainMethod.getReturnType().equals(void.class)) {
                throw new RuntimeException("could not find main method");
            }
            mainMethod.invoke(null, new Object[]{args});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.err.println(String.format("Target raised an exception: %s", e.getTargetException().getLocalizedMessage()));
        } catch (Exception e) {
            //
        }
    }
}
