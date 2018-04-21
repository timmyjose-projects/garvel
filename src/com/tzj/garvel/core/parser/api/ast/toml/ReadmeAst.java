package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

public class ReadmeAst extends ProjectMetadataAst {
    private Identifier readme;

    public ReadmeAst(final Identifier readme) {
        this.readme = readme;
    }

    @Override
    public String toString() {
        return "ReadmeAst{" +
                "readme=" + readme +
                '}';
    }

    public Identifier getReadme() {
        return readme;
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
