package com.tzj.garvel.cli.api.parser.ast;

import com.tzj.garvel.cli.api.parser.visitor.CLIAstVisitor;

public interface CLIAst {
    void accept(CLIAstVisitor visitor);
}
