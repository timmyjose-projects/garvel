package com.tzj.garvel.core.parser.api.ast.toml;

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
}
