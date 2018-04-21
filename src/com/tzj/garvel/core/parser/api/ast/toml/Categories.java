package com.tzj.garvel.core.parser.api.ast.toml;

import java.util.List;

public class Categories extends ProjectMetadataAst {
    private List<Identifier> categories;

    public Categories(final List<Identifier> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "categories=" + categories +
                '}';
    }

    public List<Identifier> getCategories() {
        return categories;
    }
}
