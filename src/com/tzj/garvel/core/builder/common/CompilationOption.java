package com.tzj.garvel.core.builder.common;

public enum CompilationOption {
    XLINT("-Xlint"),
    DEBUG("-g"),
    TARGET_DIR("-d"),
    CLASSPATH("-cp");

    private String description;

    private CompilationOption(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
