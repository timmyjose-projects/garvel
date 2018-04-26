package com.tzj.garvel.playground.javacompiler;

import java.io.*;

public class BinVsCharFilePerfDemo {
    private static final String BIN_FILE = "/Users/z0ltan/Code/Projects/Playground/temp/binfile.bin";
    private static final String TEXT_FILE = "/Users/z0ltan/Code/Projects/Playground/temp/textfile.text";

    private static final int BUF_SIZE = 8192; // 8K chunks

    public static void main(String[] args) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timeBinFileRead();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timeTextFileRead();
    }

    private static void timeTextFileRead() {
        long start = System.currentTimeMillis();

        try (BufferedReader reader = new BufferedReader(new FileReader(TEXT_FILE))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                //  do nothing
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.printf("%d seconds to read text file\n", (end - start) / 1000);
    }

    private static void timeBinFileRead() {
        long start = System.currentTimeMillis();

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(BIN_FILE))) {
            byte[] buffer = new byte[BUF_SIZE];
            int count = -1;

            while ((count = in.read(buffer)) != -1) {
                // do nothing
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.out.printf("%d seconds to read bin file\n", (end - start) / 1000);
    }
}
