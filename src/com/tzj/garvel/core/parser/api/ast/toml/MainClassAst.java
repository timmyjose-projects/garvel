package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

public class MainClassAst implements TOMLAst {
    private Identifier mainClass;

    public MainClassAst(final Identifier mainClass) {
        this.mainClass = mainClass;
    }

    public Identifier getMainClass() {
        return mainClass;
    }

    @Override
    public String toString() {
        return "MainClassAst{" +
                "mainClass=" + mainClass +
                '}';
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
