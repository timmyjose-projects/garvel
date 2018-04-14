package com.tzj.garvel.core.parser.semver;

public class SemverToken {
    private SemverTokenType kind;
    private String spelling;

    public SemverToken(final SemverTokenType kind, final String spelling) {
        this.kind = kind;
        this.spelling = spelling;
    }

    public SemverTokenType kind() {
        return kind;
    }

    public String spelling() {
        return spelling;
    }

    @Override
    public String toString() {
        return "< " + kind + ", " + spelling + " >";
    }
}
