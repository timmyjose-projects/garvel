package com.tzj.garvel.cli.api.parser.ast;

import java.util.Objects;

public class Program extends CLIAst {
    private boolean verbose;
    private boolean quiet;
    private Command command;
    public Program() {
    }

    public Program(final boolean verbose, final boolean quiet, final Command command) {
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

    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(final boolean quiet) {
        this.quiet = quiet;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(final Command command) {
        this.command = command;
    }
}
