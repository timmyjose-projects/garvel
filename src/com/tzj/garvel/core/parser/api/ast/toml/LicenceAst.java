package com.tzj.garvel.core.parser.api.ast.toml;

public class LicenceAst extends ProjectMetadataAst {
    private Identifier licence;

    public LicenceAst(final Identifier licence) {
        this.licence = licence;
    }

    @Override
    public String toString() {
        return "LicenceAst{" +
                "licence=" + licence +
                '}';
    }

    public Identifier getLicence() {
        return licence;
    }
}
