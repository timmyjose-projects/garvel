package com.tzj.garvel.core.parser.api.ast.semver;

import java.util.Objects;

public class Patch extends SemverAST {
    private IntegerLiteral patch;

    public Patch(final IntegerLiteral patch) {

        this.patch = patch;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Patch patch1 = (Patch) o;
        return Objects.equals(patch, patch1.patch);
    }

    @Override
    public int hashCode() {

        return Objects.hash(patch);
    }

    @Override
    public String toString() {
        return "Patch{" +
                "patch=" + patch +
                '}';
    }

    public IntegerLiteral getPatch() {
        return patch;
    }
}
