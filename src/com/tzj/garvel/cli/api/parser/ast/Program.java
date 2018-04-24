package com.tzj.garvel.cli.api.parser.ast;

import com.tzj.garvel.cli.api.parser.visitor.CLIAstVisitor;

import java.util.Objects;

public class Program implements CLIAst {
    private boolean verbose;
    private boolean quiet;
    private CommandAst command;

    public Program() {
    }

    public Program(final boolean verbose, final boolean quiet, final CommandAst command) {
        this.verbose = verbose;
        this.quiet = quiet;
        this.command = command;
    }

    @Override
    public String toString() {
        return "Program{" +
                "verbose=" + verbose +
                ", quiet=" + quiet +
                ", command=" + command +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Program program = (Program) o;
        return verbose == program.verbose &&
                quiet == program.quiet &&
                Objects.equals(command, program.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(verbose, quiet, command);
    }

    public boolean isVerbose() {
        return verbose;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public CommandAst getCommand() {
        return command;
    }

    public void setCommand(final CommandAst command) {
        this.command = command;
    }

    @Override
    public void accept(final CLIAstVisitor visitor) {
        visitor.visit(this);
        command.accept(visitor);
    }
}
