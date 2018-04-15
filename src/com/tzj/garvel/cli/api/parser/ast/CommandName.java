package com.tzj.garvel.cli.api.parser.ast;

import java.util.Objects;

public class CommandName extends CLIAst {
    private Identifier id;

    public CommandName(final Identifier id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CommandName{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CommandName that = (CommandName) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Identifier getId() {
        return id;

    }
}
