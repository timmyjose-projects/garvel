package com.tzj.garvel.core.parser.semver;

public enum SemverTokenType {
    IDENTIFIER("<identifier>"),
    INTLITERAL("<integer-literal>"),
    PERIOD("."),
    DASH("-"),
    PLUS("+"),
    EOT("<eot>");

    private String description;

    private SemverTokenType(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
