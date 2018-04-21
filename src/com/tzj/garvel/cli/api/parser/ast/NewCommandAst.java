package com.tzj.garvel.cli.api.parser.ast;

import com.tzj.garvel.core.engine.command.NewCommand;

import java.util.Objects;

public class NewCommandAst extends CommandAst {
    private VCSAst vcs;
    private Path path;

    public NewCommandAst() {
    }

    public NewCommandAst(final VCSAst vcs, final Path path) {
        this.vcs = vcs;
        this.path = path;
    }

    public void setPath(final Path path) {
        this.path = path;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vcs, path);
    }

    public VCSAst getVcs() {
        return vcs;
    }

    public void setVcs(final VCSAst vcs) {
        this.vcs = vcs;
    }

    @Override
    public String toString() {
        return "NewCommandAst{" +
                "vcs=" + vcs +
                ", path=" + path +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NewCommandAst that = (NewCommandAst) o;
        return Objects.equals(vcs, that.vcs) &&
                Objects.equals(path, that.path);
    }
}
