package com.tzj.garvel.playground.javacompiler;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RunClass {
    public static void main(String[] args) throws ClassNotFoundException {
        try {
            Class.forName("com.tzj.garvel.playground.javacompiler.InFile"); // works
            Class.forName("com.tzj.garvel.playground.javacompiler.RunClass$NestedStaticPrivate"); // works
            Class.forName("com.tzj.garvel.playground.javacompiler.RunClass$NestedStatic"); // works
            Class.forName("com.tzj.garvel.playground.javacompiler.RunClass$NestedPrivate"); // does not work
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        final Scanner in = new Scanner(System.in);
        final String targetClass = in.nextLine().trim();
        final List<String> cliArgsList = new ArrayList<>();

        String input = in.nextLine();
        while (!input.equalsIgnoreCase("quit")) {
            cliArgsList.add(input);
            input = in.next();
        }

        String[] mainArgs = new String[cliArgsList.size()];
        for (int i = 0; i < cliArgsList.size(); i++) {
            mainArgs[i] = cliArgsList.get(i);
        }

        final Class<?> clazz = Class.forName(targetClass);
        final Method[] methods = clazz.getDeclaredMethods();

        if (methods != null && methods.length != 0) {
            Method m = null;
            try {
                m = clazz.getMethod("main", String[].class);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }

            final int mods = m.getModifiers();

            if (Modifier.isPublic(mods) &&
                    Modifier.isStatic(mods) &&
                    m.getReturnType().equals(Void.TYPE)) {
                Constructor<?>[] cons = clazz.getConstructors();

                if (cons != null && cons.length != 0) {
                    for (int i = 0; i < cons.length; i++) {
                        final Class<?>[] params = cons[i].getParameterTypes();
                        if (params == null || params.length == 0) {
                            try {
                                Object obj = cons[i].newInstance();
                                m.invoke(obj, (Object) mainArgs);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private static class NestedStaticPrivate {
        private String name;

        public NestedStaticPrivate() {
        }

        public NestedStaticPrivate(final String name) {

        }

        public static void main(String[] args) {
            System.out.println("NestedStaticPrivate");

            for (String arg : args) {
                System.out.println(arg);
            }
        }
    }

    static class NestedStatic {
        private String name;

        public NestedStatic() {
        }

        public NestedStatic(final String name) {

        }

        public static void main(String[] args) {
            System.out.println("NestedStatic");

            for (String arg : args) {
                System.out.println(arg);
            }
        }

    }
}

class InFile {
    private String name;

    public InFile() {
    }

    public InFile(final String name) {

    }

    public static void main(String[] args) {
        System.out.println("Infile");

        for (String arg : args) {
            System.out.println(arg);
        }
    }
}
