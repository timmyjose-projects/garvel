package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

public class VersionAst implements TOMLAst {
    private Identifier version;

    public VersionAst(final Identifier version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "VersionAst{" +
                "version=" + version +
                '}';
    }

    public Identifier getVersion() {
        return version;
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
