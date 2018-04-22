package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

public class LibSectionAst implements TOMLAst {
    private MainClassAst mainClass;
    private boolean fatJar = false; // default;

    public LibSectionAst(final MainClassAst mainClass, final boolean fatJar) {
        this.mainClass = mainClass;
        this.fatJar = fatJar;
    }

    public LibSectionAst() {
        // optional
    }

    public MainClassAst getMainClass() {
        return mainClass;
    }

    public boolean isFatJar() {
        return fatJar;
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
        mainClass.accept(visitor);
    }

    @Override
    public String toString() {
        return "LibSectionAst{" +
                "mainClass=" + mainClass +
                ", fatJar=" + fatJar +
                '}';
    }
}
