package com.tzj.garvel.core.parser.api.ast.toml;

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
}
