package com.tzj.garvel.cli.api.parser.ast;

import com.tzj.garvel.core.engine.command.NewCommand;

import java.util.Objects;

public class NewCommandAst extends CommandAst {
    private VCSAst vcs;
    private boolean bin;
    private boolean lib;
    private Path path;

    public NewCommandAst() {
    }

    public NewCommandAst(final VCSAst vcs, final boolean bin, final boolean lib, final Path path) {
        this.vcs = vcs;
        this.bin = bin;
        this.lib = lib;
        this.path = path;
    }

    public void setPath(final Path path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "NewCommandAst{" +
                "vcs=" + vcs +
                ", bin=" + bin +
                ", lib=" + lib +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NewCommandAst that = (NewCommandAst) o;
        return bin == that.bin &&
                lib == that.lib &&
                Objects.equals(vcs, that.vcs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vcs, bin, lib);
    }

    public VCSAst getVcs() {

        return vcs;
    }

    public void setVcs(final VCSAst vcs) {
        this.vcs = vcs;
    }

    public boolean isBin() {
        return bin;
    }

    public void setBin(final boolean bin) {
        this.bin = bin;
    }

    public boolean isLib() {
        return lib;
    }

    public void setLib(final boolean lib) {
        this.lib = lib;
    }
}
