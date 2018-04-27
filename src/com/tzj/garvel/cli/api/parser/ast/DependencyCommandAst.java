package com.tzj.garvel.cli.api.parser.ast;

import com.tzj.garvel.cli.api.parser.visitor.CLIAstVisitor;

public class DependencyCommandAst extends CommandAst {
    private DependencyNameAst dependency;
    private String version;
    private boolean showDependencies;

    public DependencyCommandAst(final DependencyNameAst dependency, final String version, final boolean showDependencies) {
        this.dependency = dependency;
        this.version = version;
        this.showDependencies = showDependencies;
    }

    @Override
    public String toString() {
        return "DependencyCommandAst{" +
                "dependency=" + dependency +
                ", version='" + version + '\'' +
                ", showDependencies=" + showDependencies +
                '}';
    }

    public DependencyNameAst getDependency() {
        return dependency;
    }

    public String getVersion() {
        return version;
    }

    public boolean isShowDependencies() {
        return showDependencies;
    }

    @Override
    public void accept(final CLIAstVisitor visitor) {
        visitor.visit(this);
    }
}
