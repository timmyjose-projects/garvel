package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

public class NameAst implements TOMLAst {
    private Identifier name;

    public NameAst(final Identifier name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NameAst{" +
                "name=" + name +
                '}';
    }

    public Identifier getName() {
        return name;
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
