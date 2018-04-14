package com.tzj.garvel.core.parser.semver;

public class SemverToken {
    private SemverTokenType kind;
    private String spelling;
    private int columnNumber;

    public SemverToken(final SemverTokenType kind, final String spelling, final int columnNumber) {
        this.kind = kind;
        this.spelling = spelling;
        this.columnNumber = columnNumber;
    }

    public SemverTokenType kind() {
        return kind;
    }

    public String spelling() {
        return spelling;
    }

    public int columnNumber() {
        return columnNumber;
    }

    @Override
    public String toString() {
        return "< " + kind + ", " + spelling + ", " + columnNumber + " >";
    }
}
