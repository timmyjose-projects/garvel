package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

import java.util.List;

public class ClassPathAst extends ProjectMetadataAst {
    private List<Identifier> classPath;

    public ClassPathAst(final List<Identifier> classPath) {

        this.classPath = classPath;
    }

    @Override
    public String toString() {
        return "ClassPathAst{" +
                "classPath=" + classPath +
                '}';
    }

    public List<Identifier> getClassPath() {
        return classPath;
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
