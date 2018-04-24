package com.tzj.garvel.cli.api.parser.ast;

import java.util.Objects;

public class Path {
    private Identifier id;

    public Path(final Identifier id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Path{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Path path = (Path) o;
        return Objects.equals(id, path.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Identifier getId() {
        return id;
    }
}
