package com.tzj.garvel.core.dep.api.parser;

import java.util.ArrayList;
import java.util.List;

public class Versions {
    List<String> versions;

    public Versions() {
        this.versions = new ArrayList<>();
    }

    public List<String> getVersions() {
        return versions;
    }

    public void addVersion(final String version) {
        versions.add(version);
    }
}
