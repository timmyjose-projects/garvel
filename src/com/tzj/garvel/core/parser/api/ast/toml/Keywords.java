package com.tzj.garvel.core.parser.api.ast.toml;

import java.util.List;

public class Keywords extends ProjectMetadataAst {
    private List<Identifier> keywords;

    public Keywords(final List<Identifier> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "Keywords{" +
                "keywords=" + keywords +
                '}';
    }

    public List<Identifier> getKeywords() {
        return keywords;
    }
}
