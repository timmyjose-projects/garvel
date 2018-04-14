package com.tzj.garvel.core.parser.api.ast.semver;

import java.util.Objects;

public class IntegerLiteral extends SemverAST {
    private String spelling;

    public IntegerLiteral(final String spelling) {
        this.spelling = spelling;
    }

    @Override
    public String toString() {
        return "IntegerLiteral{" +
                "spelling='" + spelling + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final IntegerLiteral that = (IntegerLiteral) o;
        return Objects.equals(spelling, that.spelling);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spelling);
    }

    public String getSpelling() {
        return spelling;
    }
}
