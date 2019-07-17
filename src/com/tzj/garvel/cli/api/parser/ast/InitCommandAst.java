package com.tzj.garvel.cli.api.parser.ast;

import com.tzj.garvel.cli.api.parser.visitor.CLIAstVisitor;
import com.tzj.garvel.core.engine.command.InitCommand;

import java.util.Objects;

public class InitCommandAst extends CommandAst {
    private VCSAst vcs;
    private String currentDirectory;

    public InitCommandAst() {
    }

    public InitCommandAst(final VCSAst vcs, final String currentDirectory) {
        this.vcs = vcs;
        this.currentDirectory = currentDirectory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vcs, currentDirectory);
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(final String currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public VCSAst getVcs() {
        return vcs;
    }

    @Override
    public String toString() {
        return "InitCommandAst{" +
                "vcs=" + vcs +
                ", currentDirectory=" + currentDirectory +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final InitCommandAst that = (InitCommandAst) o;
        return Objects.equals(vcs, that.vcs) &&
                Objects.equals(currentDirectory, that.currentDirectory);
    }

    @Override
    public void accept(final CLIAstVisitor visitor) {
        visitor.visit(this);
    }
}
