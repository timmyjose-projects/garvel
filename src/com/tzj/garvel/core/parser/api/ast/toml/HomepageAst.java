package com.tzj.garvel.core.parser.api.ast.toml;

public class HomepageAst extends ProjectMetadataAst {
    private Identifier homepage;

    public HomepageAst(final Identifier homepage) {
        this.homepage = homepage;
    }

    @Override
    public String toString() {
        return "HomepageAst{" +
                "homepage=" + homepage +
                '}';
    }

    public Identifier getHomepage() {
        return homepage;
    }
}
