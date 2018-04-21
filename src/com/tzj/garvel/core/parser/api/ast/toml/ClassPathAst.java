package com.tzj.garvel.core.parser.api.ast.toml;

import java.util.List;

public class ClassPathAst extends ProjectMetadataAst {
    private List<Identifier> classPath;

    public ClassPathAst(final List<Identifier> classPath) {

        this.classPath = classPath;
    }

    public List<Identifier> getClassPath() {
        return classPath;
    }
}
