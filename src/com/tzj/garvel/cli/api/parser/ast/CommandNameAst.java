package com.tzj.garvel.cli.api.parser.ast;

import java.util.Objects;

public class CommandNameAst {
    private Identifier id;

    public CommandNameAst(final Identifier id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CommandNameAst{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CommandNameAst that = (CommandNameAst) o;
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
