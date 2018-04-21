package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

public class Identifier {
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

    public String spelling() {
        return spelling;
    }
}
