package com.tzj.garvel.core.parser.api.ast.toml;

public class ConfigAst extends TOMLAst {
    ProjectSectionAst project;
    DependenciesSectionAst dependencies;
    BinSectionAst bin;

    public ConfigAst(final ProjectSectionAst project, final DependenciesSectionAst dependencies) {
        this.project = project;
        this.dependencies = dependencies;
    }

    public ProjectSectionAst getProject() {
        return project;
    }

    public DependenciesSectionAst getDependencies() {
        return dependencies;
    }

    public BinSectionAst getBin() {
        return bin;
    }

    public void setBin(final BinSectionAst bin) {
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
