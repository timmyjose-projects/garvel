package com.tzj.garvel.core.parser.api.visitor.toml;

import com.tzj.garvel.core.parser.api.ast.toml.*;

public interface TOMLAstVisitor {
    void visit(AuthorsAst authors);

    void visit(BinPairAst target);

    void visit(BinSectionAst targets);

    void visit(CategoriesAst categories);

    void visit(ClassPathAst classpath);

    void visit(DependenciesSectionAst dependencies);

    void visit(DependencyPairAst dependency);

    void visit(DescriptionAst description);

    void visit(HomepageAst homepage);

    void visit(KeywordsAst keywords);

    void visit(LicenceAst licence);

    void visit(LicenceFileAst licenceFile);

    void visit(NameAst name);

    void visit(ReadmeAst readme);

    void visit(VersionAst version);

    void visit(MainClassAst mainClassAst);

    void visit(LibSectionAst libSectionAst);
}
