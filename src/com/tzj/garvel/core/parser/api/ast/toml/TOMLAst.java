package com.tzj.garvel.core.parser.api.ast.toml;

import com.tzj.garvel.core.parser.api.visitor.toml.TOMLAstVisitor;

public interface TOMLAst {
    void accept(TOMLAstVisitor visitor);
}
