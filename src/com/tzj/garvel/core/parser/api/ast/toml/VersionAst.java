package com.tzj.garvel.core.parser.api.ast.toml;

public class VersionAst extends TOMLAst {
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
}
