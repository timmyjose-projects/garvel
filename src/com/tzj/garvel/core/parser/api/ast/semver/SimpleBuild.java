package com.tzj.garvel.core.parser.api.ast.semver;

import java.util.Objects;

public class SimpleBuild extends Build {
    private Identifier id;

    public SimpleBuild(final Identifier id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SimpleBuild{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SimpleBuild that = (SimpleBuild) o;
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
