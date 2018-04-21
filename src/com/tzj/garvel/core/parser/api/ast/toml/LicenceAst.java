package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

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

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
