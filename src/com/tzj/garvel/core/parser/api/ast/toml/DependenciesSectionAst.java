package com.tzj.garvel.core.parser.api.ast.toml;


import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

import java.util.Set;

public class DependenciesSectionAst implements TOMLAst {
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

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);

        if (dependencies != null) {
            for (DependencyPairAst dependency : dependencies) {
                dependency.accept(visitor);
            }
        }
    }
}
