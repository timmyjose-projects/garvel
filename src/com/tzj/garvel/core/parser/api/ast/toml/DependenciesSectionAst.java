package com.tzj.garvel.core.parser.api.ast.toml;


import java.util.Set;

public class DependenciesSectionAst extends TOMLAst {
    private Set<DependencyPairAst> dependencies;

    public DependenciesSectionAst(final Set<DependencyPairAst> dependencies) {
        this.dependencies = dependencies;
    }

    public DependenciesSectionAst() {
        // no dependencies
    }

    public void setDependencies(final Set<DependencyPairAst> dependencies) {
        this.dependencies = dependencies;
    }

    public Set<DependencyPairAst> getDepedencies() {
        return dependencies;
    }

    @Override
    public String toString() {
        return "DependenciesSectionAst{" +
                "dependencies=" + dependencies +
                '}';
    }
}
