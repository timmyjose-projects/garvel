package com.tzj.garvel.core.parser.api.ast.semver;

import com.tzj.garvel.core.parser.api.visitor.semver.SemverASTVisitor;

import java.util.Objects;

public class SemverVersion extends Semver {
    private Version version;

    public SemverVersion(final Version version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SemverVersion{" +
                "version=" + version +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SemverVersion that = (SemverVersion) o;
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

    public Version getVersion() {
        return version;
    }

    @Override
    public void accept(final SemverASTVisitor visitor) {
        visitor.visit(version);
    }
}
