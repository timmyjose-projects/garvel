package com.tzj.garvel.core.parser.api.ast.semver;

import java.util.Objects;

public class SemverVersionBuild extends Semver {
    private Version version;
    private Build build;

    public SemverVersionBuild(final Version version, final Build build) {
        this.version = version;
        this.build = build;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SemverVersionBuild that = (SemverVersionBuild) o;
        return Objects.equals(version, that.version) &&
                Objects.equals(build, that.build);
    }

    @Override
    public String toString() {
        return "SemverVersionBuild{" +
                "version=" + version +
                ", build=" + build +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, build);
    }

    public Version getVersion() {
        return version;
    }

    public Build getBuild() {
        return build;
    }
}
