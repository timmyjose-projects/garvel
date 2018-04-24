package com.tzj.garvel.cli.api.parser.ast;

import com.tzj.garvel.cli.api.parser.visitor.CLIAstVisitor;

import java.util.Objects;

public class HelpCommandAst extends CommandAst {
    private CommandNameAst commandName;

    public HelpCommandAst(final CommandNameAst commandName) {
        this.commandName = commandName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final HelpCommandAst that = (HelpCommandAst) o;
        return Objects.equals(commandName, that.commandName);
    }

    @Override
    public String toString() {
        return "HelpCommandAst{" +
                "commandName=" + commandName +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandName);
    }

    public CommandNameAst getCommandName() {
        return commandName;
    }

    @Override
    public void accept(final CLIAstVisitor visitor) {
        visitor.visit(this);
    }
}
