package com.tzj.garvel.cli.api.parser.ast;

public class DependencyCommandAst extends CommandAst {
    private DependencyNameAst dependency;
    private boolean showDependencies;

    public DependencyCommandAst(final DependencyNameAst dependency, final boolean showDependencies) {
        this.dependency = dependency;
        this.showDependencies = showDependencies;
    }

    @Override
    public String toString() {
        return "DependencyCommandAst{" +
                "dependency=" + dependency +
                ", showDependencies=" + showDependencies +
                '}';
    }

    public DependencyNameAst getDependency() {
        return dependency;
    }

    public boolean isShowDependencies() {
        return showDependencies;
    }
}
