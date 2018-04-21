package com.tzj.garvel.core.parser.api.ast.semver;

import com.tzj.garvel.core.parser.api.visitor.semver.SemverASTVisitor;

import java.util.Objects;

public class SimplePreRelease extends PreRelease {
    private Identifier id;

    public SimplePreRelease(final Identifier id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SimplePreRelease{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SimplePreRelease that = (SimplePreRelease) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Identifier getId() {
        return id;
    }

    @Override
    public void accept(final SemverASTVisitor visitor) {
        visitor.visit(this);
    }
}
