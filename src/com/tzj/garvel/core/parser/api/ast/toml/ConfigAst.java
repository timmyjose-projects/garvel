package com.tzj.garvel.core.parser.api.ast.toml;

public class ConfigAst extends TOMLAst {
    ProjectSectionAst project;
    DependencyPairsAst dependencies;
    BinPairsAst bin;

    public ConfigAst(final ProjectSectionAst project, final DependencyPairsAst dependencies) {
        this.project = project;
        this.dependencies = dependencies;
    }

    public ProjectSectionAst getProject() {
        return project;
    }

    public DependencyPairsAst getDependencies() {
        return dependencies;
    }

    public BinPairsAst getBin() {
        return bin;
    }

    public void setBin(final BinPairsAst bin) {
        this.bin = bin;
    }

    @Override
    public String toString() {
        return "ConfigAst{" +
                "project=" + project +
                ", dependencies=" + dependencies +
                ", bin=" + bin +
                '}';
    }
}
