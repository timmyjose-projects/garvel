package com.tzj.garvel.core.dep.api.parser;

import java.util.ArrayList;
import java.util.List;

public class Versions {
    private String latestVersion;
    private String releaseVersion;
    private List<String> availableVersions;

    public Versions() {
        this.availableVersions = new ArrayList<>();
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(final String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getReleaseVersion() {
        return releaseVersion;
    }

    public void setReleaseVersion(final String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public List<String> getAvailableVersions() {
        return availableVersions;
    }

    public void addAvailableVersion(final String version) {
        availableVersions.add(version);
    }
}
