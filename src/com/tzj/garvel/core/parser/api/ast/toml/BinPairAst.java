package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

public class BinPairAst implements TOMLAst {
    private Identifier key;
    private Identifier value;

    public BinPairAst(final Identifier key, final Identifier value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "BinPairAst{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    public Identifier getKey() {
        return key;
    }

    public Identifier getValue() {
        return value;
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
