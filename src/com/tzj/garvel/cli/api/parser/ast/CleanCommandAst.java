package com.tzj.garvel.cli.api.parser.ast;

import com.tzj.garvel.cli.api.parser.visitor.CLIAstVisitor;

public class CleanCommandAst extends CommandAst {
    private boolean includeLogs;

    @Override
    public String toString() {
        return "CleanCommandAst{" +
                "includeLogs=" + includeLogs +
                '}';
    }

    public boolean isIncludeLogs() {
        return includeLogs;
    }

    public void setIncludeLogs(final boolean includeLogs) {
        this.includeLogs = includeLogs;
    }

    @Override
    public void accept(final CLIAstVisitor visitor) {
        visitor.visit(this);
    }
}
