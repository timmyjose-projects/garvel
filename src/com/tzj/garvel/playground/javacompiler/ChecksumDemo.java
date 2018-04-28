package com.tzj.garvel.playground.javacompiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumDemo {
    private static final int BUF_SIZE = 8192;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        final String jarPath = "/Users/z0ltan/Desktop/maven-metadata.xml";

        checkHash(jarPath, "MD5");
        checkHash(jarPath, "SHA1");
    }

    private static void checkHash(final String jarPath, final String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);

        byte[] buffer = new byte[BUF_SIZE];
        int count = -1;
        try (DigestInputStream in = new DigestInputStream(new FileInputStream(new File(jarPath)), md)) {
            while ((count = in.read(buffer)) != -1) {
                //
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] bytes = md.digest();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%02x", bytes[i] & 0xff));
        }

        System.out.printf("%s\n", sb.toString());
    }
}