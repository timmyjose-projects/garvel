package com.tzj.garvel.cli.api.parser.ast;

public class DependencyNameAst extends CLIAst {
    private Identifier commandName;

    public DependencyNameAst(final Identifier commandName) {
        this.commandName = commandName;
    }

    @Override
    public String toString() {
        return "DependencyNameAst{" +
                "commandName=" + commandName +
                '}';
    }

    public Identifier getCommandName() {
        return commandName;
    }
}
