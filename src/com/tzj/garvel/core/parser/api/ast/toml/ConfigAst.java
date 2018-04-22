package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

public class ConfigAst implements TOMLAst {
    private ProjectSectionAst project;
    private DependenciesSectionAst dependencies;
    private LibSectionAst lib;
    private BinSectionAst bin;

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

    public LibSectionAst getLib() {
        return lib;
    }

    public void setLib(final LibSectionAst lib) {
        this.lib = lib;
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
                ", lib=" + lib +
                ", bin=" + bin +
                '}';
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        // direct the traversal for the visitor
        project.accept(visitor);
        dependencies.accept(visitor);

        if (lib != null) {
            lib.accept(visitor);
        }

        if (bin != null) {
            bin.accept(visitor);
        }
    }
}
