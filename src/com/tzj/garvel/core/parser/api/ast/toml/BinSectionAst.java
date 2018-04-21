package com.tzj.garvel.core.parser.api.ast.toml;

import java.util.Set;

public class BinSectionAst extends TOMLAst {
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
}
