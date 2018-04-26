package com.tzj.garvel.cli.api.parser.ast;

import com.tzj.garvel.cli.api.parser.visitor.CLIAstVisitor;

public class CleanCommandAst extends CommandAst {
    @Override
    public String toString() {
        return "CleanCommandAst{}";
    }

    @Override
    public void accept(final CLIAstVisitor visitor) {
        visitor.visit(this);
    }
}
