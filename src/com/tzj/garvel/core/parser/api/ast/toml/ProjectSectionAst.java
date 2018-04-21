package com.tzj.garvel.core.parser.api.ast.toml;

import java.util.HashSet;
import java.util.Set;

public class ProjectSectionAst extends TOMLAst {
    private NameAst name;
    private VersionAst version;
    private Set<ProjectMetadataAst> optionals;

    public ProjectSectionAst(final NameAst name, final VersionAst version) {
        this.name = name;
        this.version = version;
        this.optionals = new HashSet<>();
    }

    public NameAst getName() {
        return name;
    }

    public VersionAst getVersion() {
        return version;
    }

    public Set<ProjectMetadataAst> getOptionals() {
        return optionals;
    }

    @Override
    public String toString() {
        return "ProjectSectionAst{" +
                "name=" + name +
                ", version=" + version +
                ", optionals=" + optionals +
                '}';
    }

    public void addOptional(ProjectMetadataAst ast) {
        optionals.add(ast);
    }
}
