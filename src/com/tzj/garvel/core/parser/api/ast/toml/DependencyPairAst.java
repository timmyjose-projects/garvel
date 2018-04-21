package com.tzj.garvel.core.parser.api.ast.toml;

public class DependencyPairAst extends TOMLAst {
    private Identifier key;
    private Identifier value;

    public DependencyPairAst(final Identifier key, final Identifier value) {

        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "DependencyPairAst{" +
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
}
