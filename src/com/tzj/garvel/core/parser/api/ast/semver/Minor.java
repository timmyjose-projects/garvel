package com.tzj.garvel.core.parser.api.ast.semver;

import java.util.Objects;

public class Minor extends SemverAST {
    private IntegerLiteral literal;

    public Minor(final IntegerLiteral literal) {
        this.literal = literal;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Minor minor = (Minor) o;
        return Objects.equals(literal, minor.literal);
    }

    @Override
    public int hashCode() {

        return Objects.hash(literal);
    }

    @Override
    public String toString() {
        return "Minor{" +
                "literal=" + literal +
                '}';
    }

    public IntegerLiteral getLiteral() {
        return literal;
    }
}
