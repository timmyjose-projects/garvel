package com.tzj.garvel.cli.api.parser.visitor;

import com.tzj.garvel.cli.api.parser.ast.*;

public interface CLIAstVisitor {
    void visit(Program program);

    void visit(BuildCommandAst buildCommand);

    void visit(CleanCommandAst cleanCommand);

    void visit(DependencyCommandAst dependencyCommand);

    void visit(HelpCommandAst helpCommand);

    void visit(InstallCommandAst installCommand);

    void visit(ListCommandAst listCommand);

    void visit(InitCommandAst initCommand);

    void visit(NewCommandAst newCommand);

    void visit(RunCommandAst runCommand);

    void visit(TestCommandAst testCommand);

    void visit(VersionCommandAst versionCommand);

    void visit(UninstallCommandAst uninstallCommandAst);
}
