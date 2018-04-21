package com.tzj.garvel.core.parser.api.ast.semver;

import java.util.Objects;

public class Identifier {
    private String spelling;

    public Identifier(final String spelling) {
        this.spelling = spelling;
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "spelling='" + spelling + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Identifier that = (Identifier) o;
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
