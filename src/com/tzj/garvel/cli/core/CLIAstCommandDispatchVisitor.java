package com.tzj.garvel.cli.core;

import com.tzj.garvel.cli.ModuleLoader;
import com.tzj.garvel.cli.api.core.*;
import com.tzj.garvel.cli.api.parser.ast.*;
import com.tzj.garvel.cli.api.parser.visitor.CLIAstVisitor;

public class CLIAstCommandDispatchVisitor implements CLIAstVisitor {
    private CLICommandOption opts;

    @Override
    public void visit(final Program program) {
        opts = new CLICommandOption(program.isVerbose(), program.isQuiet());
    }

    /**
     * Dispatch the build command to Core.
     *
     * @param buildCommand
     */
    @Override
    public void visit(final BuildCommandAst buildCommand) {
        final CLICommand build = new CLIBuildCommand(opts);
        build.execute();
    }

    /**
     * Dispatch the clean command to Core.
     *
     * @param cleanCommand
     */
    @Override
    public void visit(final CleanCommandAst cleanCommand) {
        final CLICommand clean = new CLICleanCommand(opts, cleanCommand.isIncludeLogs());
        clean.execute();
    }

    /**
     * Dispatch the dep command to Core.
     *
     * @param dependencyCommand
     */
    @Override
    public void visit(final DependencyCommandAst dependencyCommand) {
        final CLICommand dep = new CLIDepCommand(opts, dependencyCommand.getDependency().getCommandName().spelling(), dependencyCommand.isShowDependencies());
        dep.execute();
    }

    /**
     * Dispatch the help command to Core.
     *
     * @param helpCommand
     */
    @Override
    public void visit(final HelpCommandAst helpCommand) {
        final CLICommand help = new CLIHelpCommand(opts, helpCommand.getCommandName().getId().spelling());
        help.execute();
    }

    /**
     * Dispatch the install command to Core.
     *
     * @param installCommand
     */
    @Override
    public void visit(final InstallCommandAst installCommand) {
        final CLICommand install = new CLIInstallCommand(opts);
        install.execute();
    }

    /**
     * Dispatch the list commmand to Core.
     *
     * @param listCommand
     */
    @Override
    public void visit(final ListCommandAst listCommand) {
        final CLICommand list = new CLIListCommand(opts);
        list.execute();
    }

    /**
     * Dispatch the new command to Core.
     *
     * @param newCommand
     */
    @Override
    public void visit(final NewCommandAst newCommand) {
        final CLICommand novy = new CLINewCommand(opts,
                ModuleLoader.INSTANCE.getUtils().getVCSTypeFromString(newCommand.getVcs().getId().spelling()),
                newCommand.getPath().getId().spelling());
        novy.execute();
    }

    /**
     * Dispatch the run command to Core.
     *
     * @param runCommand
     */
    @Override
    public void visit(final RunCommandAst runCommand) {
        final CLICommand run = new CLIRunCommand(opts, runCommand.getTarget().getId().spelling());
        run.execute();
    }

    /**
     * Dispatch the test command to Core.
     *
     * @param testCommand
     */
    @Override
    public void visit(final TestCommandAst testCommand) {

    }

    /**
     * Dispatch the update command to Core.
     *
     * @param updateCommand
     */
    @Override
    public void visit(final UpdateCommandAst updateCommand) {

    }

    /**
     * Dispatch the version command to Core.
     *
     * @param versionCommand
     */
    @Override
    public void visit(final VersionCommandAst versionCommand) {
        final CLICommand version = new CLIVersionCommand(opts);
        version.execute();
    }

    /**
     * Dispatch the uninstall command to Core.
     *
     * @param uninstallCommandAst
     */
    @Override
    public void visit(final UninstallCommandAst uninstallCommandAst) {
        final CLICommand uninstall = new CLIUninstallCommand(opts);
        uninstall.execute();
    }
}
