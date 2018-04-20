package com.tzj.garvel.common.spi.core;

public enum VCSType {
    NONE("none"),
    GIT("git"),
    MERCURIAL("mercurial"),
    FOSSIL("fossil"),
    SVN("svn"),
    CVS("cvs");

    private String description;

    private VCSType(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
