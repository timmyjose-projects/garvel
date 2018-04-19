package com.tzj.garvel.common.util;

import com.tzj.garvel.common.spi.util.UtilService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum UtilServiceImpl implements UtilService {
    INSTANCE;

    private static final Set<String> validCommands;
    private static final double LEVENSHTEIN_THRESHOLD = 0.75;

    static {
        validCommands = new HashSet<>();

        validCommands.add("help");
        validCommands.add("version");
        validCommands.add("init");
        validCommands.add("new");
        validCommands.add("clean");
        validCommands.add("build");
        validCommands.add("run");
        validCommands.add("test");
        validCommands.add("bench");
    }

    private static double probability(final String x, final String y) {
        final int xlen = x.length();
        final int ylen = y.length();
        final int m = Math.max(xlen, ylen);

        final int l = levenshtein(x.toLowerCase(), xlen, y.toLowerCase(), ylen);
        final double p = (1.0 - (double) l / m) * 100.0;

        return p;
    }

    private static int levenshtein(final String x, final int xlen, final String y, final int ylen) {
        int[][] dp = new int[xlen + 1][ylen + 1];

        for (int i = 0; i <= xlen; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= ylen; j++) {
            dp[0][j] = j;
        }

        for (int j = 1; j <= ylen; j++) {
            for (int i = 1; i <= xlen; i++) {
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

    // test
    public static void main(String[] args) {
        final List<String> samples = Arrays.asList("neww", "ghel", "clena", "init", "testr", "buyidl", "qwert", "versiuin");

        for (String sample : samples) {
            String cmd = UtilServiceImpl.INSTANCE.findLevenshteinMatch(sample);

            if (cmd != null) {
                System.out.printf("%s is probably %s\n", sample, cmd);
            }
        }
    }

    @Override
    public String findLevenshteinMatch(final String spelling) {
        double maxp = 0.0;
        String currCommand = null;

        for (String command : validCommands) {
            double cp = probability(spelling, command);
            if (cp > maxp) {
                maxp = cp;
                currCommand = command;
            }
        }

        return currCommand;
    }
}

