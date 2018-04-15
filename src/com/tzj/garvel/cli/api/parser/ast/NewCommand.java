package com.tzj.garvel.cli.api.parser.ast;

import java.util.Objects;

public class NewCommand extends Command {
    private VCS vcs;
    private boolean bin;
    private boolean lib;
    private Path path;

    public NewCommand() {
    }

    public NewCommand(final VCS vcs, final boolean bin, final boolean lib, final Path path) {
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
        return "NewCommand{" +
                "vcs=" + vcs +
                ", bin=" + bin +
                ", lib=" + lib +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NewCommand that = (NewCommand) o;
        return bin == that.bin &&
                lib == that.lib &&
                Objects.equals(vcs, that.vcs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vcs, bin, lib);
    }

    public VCS getVcs() {

        return vcs;
    }

    public void setVcs(final VCS vcs) {
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
