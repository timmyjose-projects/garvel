package com.tzj.garvel.core.parser.api.ast.toml;

import java.util.List;

public class BinPairsAst extends TOMLAst {
    private List<BinPairAst> targets;

    public BinPairsAst(final List<BinPairAst> targets) {
        this.targets = targets;
    }

    @Override
    public String toString() {
        return "BinPairsAst{" +
                "targets=" + targets +
                '}';
    }

    public List<BinPairAst> getTargets() {
        return targets;
    }
}
