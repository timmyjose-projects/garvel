package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

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

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
