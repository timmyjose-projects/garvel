package com.tzj.garvel.core.parser.semver;

public enum SemverTokenType {
    INSTANCE("");

    private String description;

    private SemverTokenType(final String description) {
        this.description = description;
    }
}
