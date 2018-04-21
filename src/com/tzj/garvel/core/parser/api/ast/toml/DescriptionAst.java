package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

public class DescriptionAst extends ProjectMetadataAst {
    private Identifier description;

    public DescriptionAst(final Identifier description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DescriptionAst{" +
                "description=" + description +
                '}';
    }

    public Identifier getDescription() {
        return description;
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
