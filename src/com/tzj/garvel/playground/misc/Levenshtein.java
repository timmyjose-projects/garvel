package com.tzj.garvel.playground.misc;

import java.util.Scanner;

public class Levenshtein {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String first = in.next();
        String second = in.next();

        System.out.printf("Levenshtein distance = %d\n", levenshtein(first, second));
        in.close();
    }

    private static int levenshtein(final String first, final String second) {
        int flen = first.length();
        int slen = second.length();

        int[][] dp = new int[flen + 1][slen + 1];

        for (int i = 0; i <= flen; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= slen; j++) {
            dp[0][j] = j;
        }

        for (int j = 1; j <= slen; j++) {
            for (int i = 1; i <= flen; i++) {
                int insertion = dp[i][j - 1] + 1;
                int deletion = dp[i - 1][j] + 1;
                int match = dp[i - 1][j - 1];
                int mismatch = dp[i - 1][j - 1] + 1;

                if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    dp[i][j] = Math.min(Math.min(insertion, deletion), match);
                } else {
                    dp[i][j] = Math.min(Math.min(insertion, deletion), mismatch);
                }
            }
        }

        return dp[flen][slen];
    }
}
