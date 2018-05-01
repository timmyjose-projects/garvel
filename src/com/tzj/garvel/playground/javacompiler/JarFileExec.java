package com.tzj.garvel.playground.javacompiler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;

public class JarFileExec {
    private static final String JAR_FILE = "/Users/z0ltan/Code/Projects/Playground/testbed/foo/target/foo-1.2.3.jar";

    public static void main(String[] args) {
        final File jarFile = new File(JAR_FILE);
        URL jarUrl = null;

        try {
            jarUrl = jarFile.toURI().toURL();
        } catch (MalformedURLException e) {
            System.err.println("could not create url for JAR file");
        }

        final JarClassLoader loader = new JarClassLoader(jarUrl);
        String mainClassName = null;

        try {
            mainClassName = loader.getMainClassName();
        } catch (IOException e) {
            System.err.println("unable to get main class name");
        }

        Class<?> clazz = null;
        try {
            clazz = loader.getMainClass(mainClassName);
        } catch (ClassNotFoundException e) {
            System.err.println("unable to load main class");
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
            e.printStackTrace();
        }
    }
}

class JarClassLoader extends URLClassLoader {
    private URL url;

    public JarClassLoader(final URL url) {
        super(new URL[]{url});
        this.url = url;
    }

    public String getMainClassName() throws IOException {
        final URL u = new URL("jar", "", url + "!/");
        final JarURLConnection conn = (JarURLConnection) u.openConnection();
        final Attributes attr = conn.getMainAttributes();

        return attr != null ? attr.getValue(Attributes.Name.MAIN_CLASS) : null;
    }

    public Class<?> getMainClass(final String mainClassName) throws ClassNotFoundException {
        return loadClass(mainClassName);
    }
}
