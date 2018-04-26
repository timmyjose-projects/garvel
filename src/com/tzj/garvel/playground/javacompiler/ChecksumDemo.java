package com.tzj.garvel.playground.javacompiler;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumDemo {
    private static final int BUF_SIZE = 8192;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        final String jarPath = "/Users/z0ltan/Desktop/asm-3.3.1.jar";
        final String sha1Check = "1d5f20b4ea675e6fab6ab79f1cd60ec268ddc015";
        final String md5Check = "1ad1e8959324b0f680b8e62406955642";

        checkHash(jarPath, md5Check, "MD5");
        checkHash(jarPath, sha1Check, "SHA1");
    }

    private static void checkHash(final String jarPath, final String check, final String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);

        byte[] buffer = new byte[BUF_SIZE];
        int count = -1;
        try (DigestInputStream in = new DigestInputStream(new FileInputStream(jarPath), md)) {
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

        if (!check.equalsIgnoreCase(sb.toString())) {
            System.out.printf("Expected %s, found %s\n", check, sb.toString());
        }
    }
}