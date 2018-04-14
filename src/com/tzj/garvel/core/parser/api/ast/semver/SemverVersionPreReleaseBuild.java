package com.tzj.garvel.core.parser.api.ast.semver;

import java.util.Objects;

public class SemverVersionPreReleaseBuild extends Semver {
    private Version version;
    private PreRelease preRelease;
    private Build build;

    public SemverVersionPreReleaseBuild(final Version version, final PreRelease preRelease, final Build build) {
        this.version = version;
        this.preRelease = preRelease;
        this.build = build;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SemverVersionPreReleaseBuild that = (SemverVersionPreReleaseBuild) o;
        return Objects.equals(version, that.version) &&
                Objects.equals(preRelease, that.preRelease) &&
                Objects.equals(build, that.build);
    }

    @Override
    public String toString() {
        return "SemverVersionPreReleaseBuild{" +
                "version=" + version +
                ", preRelease=" + preRelease +
                ", build=" + build +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, preRelease, build);
    }

    public Version getVersion() {

        return version;
    }

    public PreRelease getPreRelease() {
        return preRelease;
    }

    public Build getBuild() {
        return build;
    }
}
