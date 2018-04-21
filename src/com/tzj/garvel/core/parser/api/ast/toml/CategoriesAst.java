package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

import java.util.List;

public class CategoriesAst extends ProjectMetadataAst {
    private List<Identifier> categories;

    public CategoriesAst(final List<Identifier> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CategoriesAst{" +
                "categories=" + categories +
                '}';
    }

    public List<Identifier> getCategories() {
        return categories;
    }

    @Override
    public void accept(final TOMLAstVisitor visitor) {
        visitor.visit(this);
    }
}
