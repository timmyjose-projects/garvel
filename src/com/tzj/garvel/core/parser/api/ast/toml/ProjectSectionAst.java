package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

import java.util.HashSet;
import java.util.Set;

public class ProjectSectionAst implements TOMLAst {
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

    public void setOptionals(final Set<ProjectMetadataAst> optionals) {
        this.optionals = optionals;
    }

    @Override
    public String toString() {
        return "ProjectSectionAst{" +
                "name=" + name +
                ", version=" + version +
                ", optionals=" + optionals +
                '}';
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        name.accept(visitor);
        version.accept(visitor);

        if (optionals != null) {
            for (ProjectMetadataAst option : optionals) {
                option.accept(visitor);
            }
        }
    }
}
