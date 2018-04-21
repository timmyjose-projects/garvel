package com.tzj.garvel.core.parser.api.ast.toml;

public class Identifier extends TOMLAst {
    private String spelling;

    public Identifier(final String spelling) {

        this.spelling = spelling;
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "spelling='" + spelling + '\'' +
                '}';
    }

    public String getSpelling() {
        return spelling;
    }
}
