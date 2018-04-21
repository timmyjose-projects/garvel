package com.tzj.garvel.core.parser.api.ast.toml;

import java.util.List;

public class DependencyPairsAst extends TOMLAst {
    private List<DependencyPairAst> dependencies;

    public DependencyPairsAst(final List<DependencyPairAst> dependencies) {

        this.dependencies = dependencies;
    }

    public List<DependencyPairAst> getDepedencies() {
        return dependencies;
    }

    @Override
    public String toString() {
        return "DependencyPairsAst{" +
                "dependencies=" + dependencies +
                '}';
    }
}
