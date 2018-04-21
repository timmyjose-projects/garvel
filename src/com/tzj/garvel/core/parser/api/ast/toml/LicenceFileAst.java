package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

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

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
