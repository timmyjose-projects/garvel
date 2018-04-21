package com.tzj.garvel.core.parser.api.ast.toml;

public class Readme extends ProjectMetadataAst {
    private Identifier readme;

    public Readme(final Identifier readme) {
        this.readme = readme;
    }

    @Override
    public String toString() {
        return "Readme{" +
                "readme=" + readme +
                '}';
    }

    public Identifier getReadme() {
        return readme;
    }
}
