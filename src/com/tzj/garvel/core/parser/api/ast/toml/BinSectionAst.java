package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

import java.util.Set;

public class BinSectionAst implements TOMLAst {
    private Set<BinPairAst> targets;

    public BinSectionAst(final Set<BinPairAst> targets) {
        this.targets = targets;
    }

    public BinSectionAst() {
        // optional
    }

    @Override
    public String toString() {
        return "BinSectionsAst{" +
                "targets=" + targets +
                '}';
    }

    public Set<BinPairAst> getTargets() {
        return targets;
    }

    public void setTargets(final Set<BinPairAst> targets) {
        this.targets = targets;
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);

        if (targets != null) {
            for (BinPairAst target : targets) {
                target.accept(visitor);
            }
        }
    }
}
