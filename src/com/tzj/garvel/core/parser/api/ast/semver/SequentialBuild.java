package com.tzj.garvel.core.parser.api.ast.semver;

import com.tzj.garvel.core.parser.api.visitor.semver.SemverASTVisitor;

import java.util.Objects;

public class SequentialBuild extends Build {
    private Build build1;
    private Build build2;

    public SequentialBuild(final Build build1, final Build build2) {

        this.build1 = build1;
        this.build2 = build2;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SequentialBuild that = (SequentialBuild) o;
        return Objects.equals(build1, that.build1) &&
                Objects.equals(build2, that.build2);
    }

    @Override
    public String toString() {
        return "SequentialBuild{" +
                "build1=" + build1 +
                ", build2=" + build2 +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(build1, build2);
    }

    public Build getBuild1() {

        return build1;
    }

    public Build getBuild2() {
        return build2;
    }

    @Override
    public void accept(final SemverASTVisitor visitor) {
        visitor.visit(build1);
        visitor.visit(build2);
    }
}
