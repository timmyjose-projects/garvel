package com.tzj.garvel.common.spi.util;

import com.tzj.garvel.common.spi.core.VCSType;

import java.nio.file.Path;
import java.util.List;

/**
 * Common utlities for use by Garvel core and Garvel CLI/GUI.
 */
public interface UtilService {
    String findLevenshteinMatchCommand(final String spelling);

    String findLevenshteinMatchVCS(final String spelling);

    VCSType getVCSTypeFromString(String spelling);

    void displayFormattedToConsole(boolean newline, String format, Object... values);

    boolean validateArtifactVersion(String version);

    String getMD5(Path path);

    String getSHA1(Path path);

    String convertStringsToOSSpecificClassPathString(List<String> paths);

    String getCurrentDirectory();
}

