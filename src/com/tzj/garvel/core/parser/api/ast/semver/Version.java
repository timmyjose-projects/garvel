package com.tzj.garvel.core.parser.api.ast.semver;

import java.util.Objects;

public class Version extends SemverAST {
    private Major major;
    private Minor minor;
    private Patch patch;

    public Version(final Major major, final Minor minor, final Patch patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    @Override
    public String toString() {
        return "Version{" +
                "major=" + major +
                ", minor=" + minor +
                ", patch=" + patch +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Version version = (Version) o;
        return Objects.equals(major, version.major) &&
                Objects.equals(minor, version.minor) &&
                Objects.equals(patch, version.patch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }

    public Major getMajor() {
        return major;
    }

    public Minor getMinor() {
        return minor;
    }

    public Patch getPatch() {
        return patch;
    }
}
