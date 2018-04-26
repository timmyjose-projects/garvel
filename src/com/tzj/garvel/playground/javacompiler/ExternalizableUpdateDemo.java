package com.tzj.garvel.playground.javacompiler;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ExternalizableUpdateDemo {
    public static void main(String[] args) {
        Map<String, String> cache = new HashMap<>();

        cache.put("one", Paths.get("foo").toString());
        cache.put("two", Paths.get("bar").toString());

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("/tmp/cache.bin"))) {
            out.writeObject(cache);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> oldCache = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("/tmp/cache.bin"))) {
            oldCache = (HashMap<String, String>) in.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        oldCache.put("three", Paths.get("bar").toString());
        oldCache.remove("two");

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("/tmp/cache.bin"))) {
            out.writeObject(oldCache);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> finalCache = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("/tmp/cache.bin"))) {
            finalCache = (HashMap<String, String>) in.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        display(finalCache);
    }

    private static void display(final Map<String, String> cache) {
        for (Map.Entry<String, String> entry : cache.entrySet()) {
            System.out.printf("%s : %s\n", entry.getKey(), entry.getValue());
        }
    }
}
