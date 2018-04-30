package com.tzj.garvel.playground.javacompiler;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

public class CustomClassLoad {
    public static void main(String[] args) throws MalformedURLException {
        Scanner in = new Scanner(System.in);

        final String classPath = in.nextLine().trim();
        final String className = in.nextLine().trim();

        final File srcPath = new File(classPath);
        final URL url = srcPath.toURI().toURL();
        final URL[] urls = new URL[]{url};

        ClassLoader loader = new URLClassLoader(urls);
        Class<?> clazz = null;
        try {
            clazz = loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Loaded " + clazz.getCanonicalName());
    }
}
