package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.semver.SemverKey;
import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyPairAst implements TOMLAst {
    private Identifier key;
    private Map<SemverKey, List<String>> value;

    public DependencyPairAst(final Identifier key) {
        this.key = key;
        this.value = new HashMap<>();
    }

    public Map<SemverKey, List<String>> getValue() {
        return value;
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

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
