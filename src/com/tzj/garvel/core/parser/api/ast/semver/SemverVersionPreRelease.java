package com.tzj.garvel.core.parser.api.ast.semver;

import java.util.Objects;

public class SemverVersionPreRelease extends Semver {
    private Version version;
    private PreRelease preRelease;

    public SemverVersionPreRelease(final Version version, final PreRelease preRelease) {
        this.version = version;
        this.preRelease = preRelease;
    }

    @Override
    public String toString() {
        return "SemverVersionPreRelease{" +
                "version=" + version +
                ", preRelease=" + preRelease +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SemverVersionPreRelease that = (SemverVersionPreRelease) o;
        return Objects.equals(version, that.version) &&
                Objects.equals(preRelease, that.preRelease);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, preRelease);
    }

    public Version getVersion() {

        return version;
    }

    public PreRelease getPreRelease() {
        return preRelease;
    }
}
