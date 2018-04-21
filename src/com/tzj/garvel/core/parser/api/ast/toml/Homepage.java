package com.tzj.garvel.core.parser.api.ast.toml;

public class Homepage extends ProjectMetadataAst {
    private Identifier homepage;

    public Homepage(final Identifier homepage) {
        this.homepage = homepage;
    }

    @Override
    public String toString() {
        return "Homepage{" +
                "homepage=" + homepage +
                '}';
    }

    public Identifier getHomepage() {
        return homepage;
    }
}
