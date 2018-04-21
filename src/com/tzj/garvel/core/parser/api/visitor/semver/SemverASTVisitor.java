package com.tzj.garvel.core.parser.api.visitor.semver;

import com.tzj.garvel.core.parser.api.ast.semver.*;

public interface SemverASTVisitor {
    void visit(Major majorAst);

    void visit(Minor minorAst);

    void visit(Patch patchAst);

    void visit(SemverVersion semverVersionAst);

    void visit(SemverVersionBuild semverVersionBuildAst);

    void visit(SemverVersionPreRelease semverVersionPreReleaseAst);

    void visit(SemverVersionPreReleaseBuild semverVersionPreReleaseBuildAst);

    void visit(SequentialPreRelease sequentialPreReleaseAst);

    void visit(SimpleBuild simpleBuildAst);

    void visit(Version versionAst);

    void visit(SimplePreRelease simplePreReleaseAst);

    void visit(SequentialBuild sequentialBuild);

    void visit(Build build);

    void visit(PreRelease preRelease);
}
