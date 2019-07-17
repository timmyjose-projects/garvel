package com.tzj.garvel.common.util;

import com.tzj.garvel.common.spi.core.CoreService;
import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.core.VCSType;
import com.tzj.garvel.common.spi.util.UtilService;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.CoreServiceImpl;
import com.tzj.garvel.core.filesystem.api.OsType;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.tzj.garvel.common.spi.core.VCSType.*;

public enum UtilServiceImpl implements UtilService {
    INSTANCE;

    private static final Map<String, VCSType> validVCS;
    private static final Set<String> validCommands;
    private static final double LEVENSHTEIN_THRESHOLD = 0.50;
    private static final int BUF_SIZE = 8192; // 8KB

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

    /**
     * Display the given String on the console.
     *
     * @param newline
     * @param format
     * @param values
     */
    @Override
    public void displayFormattedToConsole(boolean newline, final String format, final Object... values) {
        if (newline) {
            System.out.println(String.format(format, values));
        } else {
            System.out.print(String.format(format, values));
        }
    }

    /**
     * A valid jar version is in the "[0-9]+.[0-9]+.[0-9]*" format.
     * Since this can vary considerablt, only basic checks are provided here,
     * offloading proper validation onto Core.
     *
     * @param version
     * @return
     */
    @Override
    public boolean validateArtifactVersion(final String version) {
        if (version.isEmpty()) {
            return false;
        }

        if (!version.contains(".")) {
            return false;
        }

        // check if it starts with a numerical value
        if (!Character.isDigit(version.charAt(0))) {
            return false;
        }

        return true;
    }

    /**
     * Generate the MD5 Hash of the given path.
     *
     * @param path
     * @return
     */
    @Override
    public String getMD5(final Path path) {
        return getHash(path, "MD5");
    }

    /**
     * Generate the SHA1 hash of the given path.
     *
     * @param path
     * @return
     */
    @Override
    public String getSHA1(final Path path) {
        return getHash(path, "SHA1");
    }

    /**
     * Conver the given absolute paths to platform-specific classpath entries.
     *
     * @param paths
     * @return
     */
    @Override
    public String convertStringsToOSSpecificClassPathString(final List<String> paths) {
        StringBuffer sb = new StringBuffer();

        for (final String path : paths) {
            sb.append(path);
            sb.append(File.pathSeparator);
        }

        return sb.toString();
    }

    private String getHash(final Path path, final String algorithm) {
        try {
            final StringBuffer sb = new StringBuffer();
            final MessageDigest md = MessageDigest.getInstance(algorithm);
            final DigestInputStream in = new DigestInputStream(new FileInputStream(path.toFile()), md);

            byte[] buffer = new byte[BUF_SIZE];
            while (in.read(buffer) != -1) {
                //
            }

            in.close();

            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02x", bytes[i] & 0xff));
            }

            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Simply check that the given path exists and is valid.
     *
     * @param path
     * @return pathExists
     */
    public boolean pathExists(final Path path) {
        if (path == null) {
            return false;
        }

        return path.toFile().exists();
    }

    /**
     * Extract the name of the currenr directory.
     *
     * @return currentDirectory
     */
    public final String getCurrentDirectory() {
        final String currentPath = System.getProperty("user.dir");

        return currentPath.substring(currentPath.lastIndexOf("/") + 1);
    }
}

