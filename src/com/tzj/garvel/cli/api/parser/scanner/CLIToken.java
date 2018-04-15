package com.tzj.garvel.cli.api.parser.scanner;

import com.tzj.garvel.cli.exception.CLIScannerException;

import java.util.Objects;

public class CLIToken {
    private CLITokenType kind;
    private String spelling;
    private int column;

    public CLIToken(final CLITokenType kind, final String spelling, final int column) {
        this.kind = kind;

        if (CLITokenType.isKeyword(spelling)) {
            this.kind = CLITokenType.getKeyword(spelling);
            if (this.kind == CLITokenType.UNKNOWN) {
                // @TODO elegant and useful error-handling
            }
        }
        this.spelling = spelling;
        this.column = column;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CLIToken cliToken = (CLIToken) o;
        return column == cliToken.column &&
                kind == cliToken.kind &&
                Objects.equals(spelling, cliToken.spelling);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, spelling, column);
    }

    @Override
    public String toString() {
        return "< " + kind + ", " + spelling + ", " + column + " >";
    }

    public CLITokenType kind() {
        return kind;
    }

    public String spelling() {
        return spelling;
    }

    public int column() {
        return column;
    }
}
