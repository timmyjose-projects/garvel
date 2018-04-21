package com.tzj.garvel.core.parser.api.ast.toml;

public class Licence extends ProjectMetadataAst {
    private Identifier licence;

    public Licence(final Identifier licence) {
        this.licence = licence;
    }

    @Override
    public String toString() {
        return "Licence{" +
                "licence=" + licence +
                '}';
    }

    public Identifier getLicence() {
        return licence;
    }
}
