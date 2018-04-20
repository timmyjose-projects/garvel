package com.tzj.garvel.cli.api.parser.ast;

import java.util.Objects;

public class VCSAst extends CLIAst {
    private Identifier id;

    public VCSAst(final Identifier id) {

        this.id = id;
    }

    @Override
    public String toString() {
        return "VCSAst{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VCSAst vcs = (VCSAst) o;
        return Objects.equals(id, vcs.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Identifier getId() {
        return id;
    }
}
