package com.tzj.garvel.cli.api.parser.ast;

import java.util.Objects;

public class TargetNameAst {
    private Identifier id;

    public TargetNameAst(final Identifier id) {
        this.id = id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TargetNameAst that = (TargetNameAst) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return "TargetNameAst{" +
                "id=" + id +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Identifier getId() {
        return id;
    }
}
