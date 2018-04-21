package com.tzj.garvel.core.parser.api.ast.semver;

import com.tzj.garvel.core.parser.api.visitor.semver.SemverASTVisitor;

import java.util.Objects;

public class SequentialPreRelease extends PreRelease {
    private PreRelease preRelease1;
    private PreRelease preRelease2;

    public SequentialPreRelease(final PreRelease preRelease1, final PreRelease preRelease2) {

        this.preRelease1 = preRelease1;
        this.preRelease2 = preRelease2;
    }

    @Override
    public String toString() {
        return "SequentialPreRelease{" +
                "preRelease1=" + preRelease1 +
                ", preRelease2=" + preRelease2 +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SequentialPreRelease that = (SequentialPreRelease) o;
        return Objects.equals(preRelease1, that.preRelease1) &&
                Objects.equals(preRelease2, that.preRelease2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preRelease1, preRelease2);
    }

    public PreRelease getPreRelease1() {

        return preRelease1;
    }

    public PreRelease getPreRelease2() {
        return preRelease2;
    }

    @Override
    public void accept(final SemverASTVisitor visitor) {
        visitor.visit(preRelease1);
        visitor.visit(preRelease2);
    }
}
