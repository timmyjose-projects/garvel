package com.tzj.garvel.common.util;

import com.tzj.garvel.common.spi.core.CoreService;
import com.tzj.garvel.common.spi.core.VCSType;
import com.tzj.garvel.common.spi.util.UtilService;
import com.tzj.garvel.core.CoreServiceImpl;

import java.nio.file.Path;
import java.util.*;

import static com.tzj.garvel.common.spi.core.VCSType.*;

public enum UtilServiceImpl implements UtilService {
    INSTANCE;

    private static final Map<String, VCSType> validVCS;
    private static final Set<String> validCommands;
    private static final double LEVENSHTEIN_THRESHOLD = 0.50;

    static {
        validVCS = new HashMap<>();

        validVCS.put("git", GIT);
        validVCS.put("fossil", FOSSIL);
        validVCS.put("mercurial", MERCURIAL);
        validVCS.put("svn", SVN);
        validVCS.put("cvs", CVS);
    }

    static {
        validCommands = new HashSet<>();

        validCommands.add("help");
        validCommands.add("version");
        validCommands.add("list");
        validCommands.add("install");
        validCommands.add("uninstall");
        validCommands.add("new");
        validCommands.add("clean");
        validCommands.add("build");
        validCommands.add("run");
        validCommands.add("dep");
    }

    private static double probability(final String x, final String y) {
        final int xlen = x.length();
        final int ylen = y.length();
        final int m = Math.max(xlen, ylen);

        final int l = levenshtein(x.toLowerCase(), xlen, y.toLowerCase(), ylen);
        final double p = (1.0 - (double) l / m);

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
            String cmd = UtilServiceImpl.INSTANCE.findLevenshteinMatchCommand(sample);

            if (cmd != null) {
                System.out.printf("%s is probably %s\n", sample, cmd);
            }
        }
    }

    /**
     * Find the closest match (command) for the given string, or return null.
     *
     * @param spelling
     * @return
     */
    @Override
    public String findLevenshteinMatchCommand(final String spelling) {
        double maxp = 0.0;
        String currCommand = null;

        for (String command : validCommands) {
            double cp = probability(spelling, command);
            if (cp > maxp) {
                maxp = cp;
                currCommand = command;
            }
        }

        if (maxp > LEVENSHTEIN_THRESHOLD) {
            return currCommand;
        }

        return null;
    }

    /**
     * Find the closest match (vcs) for the given string, or return null.
     *
     * @param spelling
     * @return
     */
    @Override
    public String findLevenshteinMatchVCS(final String spelling) {
        double maxp = 0.0;
        String currVcs = null;

        for (String vcs : validVCS.keySet()) {
            double cp = probability(spelling, vcs);
            if (cp > maxp) {
                maxp = cp;
                currVcs = vcs;
            }
        }

        if (maxp > LEVENSHTEIN_THRESHOLD) {
            return currVcs;
        }

        return null;
    }


    @Override
    public VCSType getVCSTypeFromString(final String spelling) {
        VCSType vcs = validVCS.get(spelling);
        if (vcs == null) {
            vcs = VCSType.NONE;
        }

        return vcs;
    }

    @Override
    public void displayFormattedToConsole(boolean newline, final String format, final Object... values) {
        if (newline) {
            System.out.println(String.format(format, values));
        } else {
            System.out.print(String.format(format, values));
        }
    }

    /**
     * Simply check that the given path exists and is valid.
     *
     * @param path
     * @return
     */
    public boolean pathExists(final Path path) {
        return path.toFile().exists();
    }
}

