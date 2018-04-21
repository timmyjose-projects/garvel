package com.tzj.garvel.core.parser.api.ast.toml;

public class LicenceFile extends ProjectMetadataAst {
    private Identifier licenceFile;

    public LicenceFile(final Identifier licenceFile) {
        this.licenceFile = licenceFile;
    }

    @Override
    public String toString() {
        return "LicenceFile{" +
                "licenceFile=" + licenceFile +
                '}';
    }

    public Identifier getLicenceFile() {
        return licenceFile;
    }
}
