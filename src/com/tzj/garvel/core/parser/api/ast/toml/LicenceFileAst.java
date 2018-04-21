package com.tzj.garvel.core.parser.api.ast.toml;

public class LicenceFileAst extends ProjectMetadataAst {
    private Identifier licenceFile;

    public LicenceFileAst(final Identifier licenceFile) {
        this.licenceFile = licenceFile;
    }

    @Override
    public String toString() {
        return "LicenceFileAst{" +
                "licenceFile=" + licenceFile +
                '}';
    }

    public Identifier getLicenceFile() {
        return licenceFile;
    }
}
