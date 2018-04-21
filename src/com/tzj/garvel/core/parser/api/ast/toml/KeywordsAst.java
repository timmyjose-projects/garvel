package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

import java.util.List;

public class KeywordsAst extends ProjectMetadataAst {
    private List<Identifier> keywords;

    public KeywordsAst(final List<Identifier> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "KeywordsAst{" +
                "keywords=" + keywords +
                '}';
    }

    public List<Identifier> getKeywords() {
        return keywords;
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
