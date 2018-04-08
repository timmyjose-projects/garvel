package com.tzj.garvel.playground.misc;

import java.util.Scanner;

public class StringSimilarity {
    private static int levenshtein(final String x, int xlen, final String y, int ylen) {
        int[][] dp = new int[xlen + 1][ylen + 1];

        for (int i = 0; i < xlen + 1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j < ylen + 1; j++) {
            dp[0][j] = j;
        }

        for (int j = 1; j < ylen + 1; j++) {
            for (int i = 1; i < xlen + 1; i++) {
                int insertion = dp[i][j - 1] + 1;
                int deletion = dp[i - 1][j] + 1;
                int match = dp[i - 1][j - 1];
                int mismatch = dp[i - 1][j - 1] + 1;

                if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    dp[i][j] = Math.min(Math.min(insertion, deletion), match);
                } else {
                    dp[i][j] = Math.min(Math.min(insertion, deletion), mismatch);
                }
            }
        }

        return dp[xlen][ylen];
    }

    private static double probability(final String x, final String y) {
        int xlen = x.length();
        int ylen = y.length();
        int m = Math.max(xlen, ylen);

        int l = levenshtein(x.toLowerCase(), xlen, y.toLowerCase(), ylen);
        double p = (1.0 - (double)l / m) * 100.0;

        return p;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String x = in.next();
        String y = in.next();

        System.out.printf("%s and %s have %.2f probability of being the same string\n", x, y, probability(x, y));
    }
}
