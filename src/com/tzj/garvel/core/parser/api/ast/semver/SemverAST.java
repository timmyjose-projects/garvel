package com.tzj.garvel.core.parser.api.ast.semver;

import com.tzj.garvel.core.parser.api.visitor.semver.SemverASTVisitor;

public interface SemverAST {
    void accept(SemverASTVisitor visitor);
}
