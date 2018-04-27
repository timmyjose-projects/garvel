package com.tzj.garvel.cli.parser;

import com.tzj.garvel.cli.ModuleLoader;
import com.tzj.garvel.cli.api.parser.CLIParser;
import com.tzj.garvel.cli.api.parser.ast.*;
import com.tzj.garvel.cli.api.parser.scanner.CLIToken;
import com.tzj.garvel.cli.api.parser.scanner.CLITokenType;
import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.cli.exception.CLIException;
import com.tzj.garvel.cli.parser.scanner.CLIScanner;
import com.tzj.garvel.common.spi.core.VCSType;
import com.tzj.garvel.common.util.UtilServiceImpl;

import static com.tzj.garvel.cli.api.parser.scanner.CLITokenType.EOT;
import static com.tzj.garvel.cli.api.parser.scanner.CLITokenType.IDENTIFIER;
import static com.tzj.garvel.cli.api.parser.scanner.CLITokenType.SHOW_DEPENDENCIES;
import static com.tzj.garvel.common.parser.GarvelConstants.SPACE;
import static com.tzj.garvel.common.spi.core.VCSType.NONE;

public enum CLIParserImpl implements CLIParser {
    INSTANCE;

    private CLIScanner scanner;
    private CLIToken currentToken;

    // test
    public static void main(String[] args) {
        //final String[] input = new String[]{"--verbose", "new", "--vcs", "git", "foo"};
        //final String[] input = new String[]{"help", "new"};
        //final String[] input = new String[]{"--verbose", "run", "one"};
        //final String[] input = new String[]{"-q", "dep", "junit"};
        //final String[] input = new String[]{"-q", "clean", "--include-logs"};
        final String[] input = new String[]{"run"};

        Program program = CLIParserImpl.INSTANCE.parse(input);
        System.out.println(program);
    }

    /**
     * Unconditionally accept the current token.
     */
    private void acceptIt() {
        currentToken = scanner.scan();
    }

    /**
     * Accept the current token only if it is the expected kind.
     *
     * @param expectedKind
     */
    private void accept(final CLITokenType expectedKind) throws CLIException {
        if (currentToken.kind() != expectedKind) {
            throw new CLIException(String.format("CLI Parser Error at col %d. Expected to accept token of kind %s, found token of kind %s",
                    currentToken.column(), expectedKind, currentToken.kind()));
        }

        if (currentToken.kind() == EOT) {
            return;
        }

        currentToken = scanner.scan();
    }

    /**
     * The starting rule,
     * <p>
     * Program ::= (VCS | VCS LIB | VCS BIN) Command
     *
     * @return
     */
    Program parseProgram() {
        Program program = null;

        switch (currentToken.kind()) {
            case VERBOSE: {
                acceptIt();
                final CommandAst command = parseCommand();

                program = new Program(true, false, command);
            }
            break;

            case QUIET: {
                acceptIt();
                final CommandAst command = parseCommand();

                program = new Program(false, true, command);
            }
            break;

            case HELP:
            case LIST:
            case VERSION:
            case INSTALL:
            case UNINSTALL:
            case NEW:
            case BUILD:
            case CLEAN:
            case RUN:
            case DEP:
            case TEST: {
                final CommandAst command = parseCommand();

                // default quiet
                program = new Program(false, true, command);
            }
            break;

            default: {
                final String bestMatch = ModuleLoader.INSTANCE.getUtils().findLevenshteinMatchCommand(currentToken.spelling());

                if (bestMatch != null) {
                    CLIErrorHandler.errorAndExit("\"%s\" is not a valid command.\n Did you mean \"%s\"?",
                            currentToken.spelling(), ModuleLoader.INSTANCE.getUtils().findLevenshteinMatchCommand(currentToken.spelling()));
                } else {
                    CLIErrorHandler.errorAndExit("%s is not a valid command.",
                            currentToken.spelling());
                }
            }
        }

        return program;
    }

    /**
     * Command ::= HelpCommand | ListCommand | VersionCommand | NewCommand
     * | BuildCommand | CleanCommand | RunComman | TestCommand
     *
     * @return
     */
    private CommandAst parseCommand() {
        CommandAst command = null;

        switch (currentToken.kind()) {
            case HELP: {
                acceptIt();
                final CommandNameAst commandName = parseCommandName();
                command = new HelpCommandAst(commandName);
            }
            break;

            case LIST: {
                acceptIt();
                command = new ListCommandAst();
            }
            break;

            case VERSION: {
                acceptIt();
                command = new VersionCommandAst();
            }
            break;

            case INSTALL: {
                acceptIt();
                command = new InstallCommandAst();
            }
            break;

            case UNINSTALL: {
                acceptIt();
                command = new UninstallCommandAst();
            }
            break;

            case NEW: {
                acceptIt();

                switch (currentToken.kind()) {
                    case VCS: {
                        acceptIt();
                        final VCSAst vcs = parseVCS();
                        final Path path = parsePath();
                        command = new NewCommandAst(vcs, path);
                    }
                    break;

                    case IDENTIFIER: {
                        final Path path = parsePath();
                        command = new NewCommandAst(new VCSAst(new Identifier(NONE.toString())), path);
                    }
                    break;

                    default: {
                        CLIErrorHandler.errorAndExit("Error: Either %s, or the PATH must follow the `new` command",
                                CLITokenType.VCS);
                    }
                }
            }
            break;

            case BUILD: {
                acceptIt();
                command = new BuildCommandAst();
            }
            break;

            case CLEAN: {
                acceptIt();
                command = new CleanCommandAst();
            }
            break;

            case RUN: {
                acceptIt();

                final RunCommandAst runCommand = new RunCommandAst();
                if (currentToken.kind() == IDENTIFIER) {
                    final TargetNameAst target = parseTargetName();
                    runCommand.setTarget(target);
                }

                command = runCommand;
            }
            break;

            case DEP: {
                acceptIt();

                boolean showDependencies = false; // default
                String version = null;
                if (currentToken.kind() == SHOW_DEPENDENCIES) {
                    acceptIt();
                    showDependencies = true;
                    version = parseDependencyVersion();
                }

                final DependencyNameAst artifact = parseDependencyName();
                command = new DependencyCommandAst(artifact, version, showDependencies);
            }
            break;

            case TEST: {
                acceptIt();
                command = new TestCommandAst();
            }
            break;
        }

        return command;
    }

    private String parseDependencyVersion() {
        String version = null;
        try {
            version = parseIdentifier().spelling();
        } catch (CLIException e) {
            CLIErrorHandler.errorAndExit("Missing version for the --show-dependencies option.");
        }

        // double-check to ensure that version is not conflated with PATh
        if (!UtilServiceImpl.INSTANCE.validateArtifactVersion(version)) {
            CLIErrorHandler.errorAndExit("\"%s\" does not appear to be a valid artifact version.", version);
        }

        return version;
    }

    /**
     * Dependency-Name ::= Identifier
     *
     * @return
     */
    private DependencyNameAst parseDependencyName() {
        DependencyNameAst depName = null;
        Identifier depNameId = null;
        try {
            depNameId = parseIdentifier();
        } catch (CLIException e) {
            CLIErrorHandler.errorAndExit("Missing artifact name for `dep` command");
        }

        // validate that it is the proper format - groupId/artifactId.
        String[] parts = depNameId.spelling().split("/");
        if (parts == null || parts.length != 2 || parts[0] == null || parts[0].isEmpty() || parts[1] == null || parts[1].isEmpty()) {
            CLIErrorHandler.errorAndExit("Supplied dependency name %s is not in the proper `groupId/artifactId` format", depNameId.spelling());
        }

        depName = new DependencyNameAst(parts[0], parts[1]);

        return depName;
    }

    /**
     * TargetName ::= Identifier
     *
     * @return
     */
    private TargetNameAst parseTargetName() {
        TargetNameAst targetName = null;
        try {
            targetName = new TargetNameAst(parseIdentifier());
        } catch (CLIException e) {
            CLIErrorHandler.errorAndExit("Missing target name for `run` command");
        }

        return targetName;
    }

    /**
     * Path ::= Identifier
     *
     * @return
     */
    private Path parsePath() {
        Path path = null;
        try {
            path = new Path(parseIdentifier());
        } catch (CLIException e) {
            CLIErrorHandler.errorAndExit("Unable to parse `path` for `new` command");
        }

        return path;
    }

    /**
     * VCS ::= Identifier
     *
     * @return
     */
    private VCSAst parseVCS() {
        VCSAst vcs = null;

        try {
            vcs = new VCSAst(parseIdentifier());
        } catch (CLIException e) {
            CLIErrorHandler.errorAndExit("Unable to parse `vcs` for `new` command");
        }

        // validate vcs type here rather than delaying it for later.
        final String vcsTypeString = vcs.getId().spelling();
        final VCSType vcsType = UtilServiceImpl.INSTANCE.getVCSTypeFromString(vcsTypeString);

        if (vcsType == NONE) {
            final String mostProbableVcs = UtilServiceImpl.INSTANCE.findLevenshteinMatchVCS(vcsTypeString);
            if (mostProbableVcs != null) {
                CLIErrorHandler.errorAndExit("Unknown vcs type: \"%s\". Did you mean \"%s\"?\n", vcsTypeString, mostProbableVcs);
            } else {
                CLIErrorHandler.errorAndExit("Unknown vcs type: \"%s\". Try `garvel help new` for the complete list of valid VCS types.\n", vcsTypeString);
            }
        }

        return vcs;
    }

    /**
     * CommandName ::= Identifier
     *
     * @return
     */
    private CommandNameAst parseCommandName() {
        switch (currentToken.kind()) {
            case HELP:
            case LIST:
            case VERSION:
            case INSTALL:
            case UNINSTALL:
            case NEW:
            case CLEAN:
            case BUILD:
            case RUN:
            case DEP:
            case TEST:
                break;
            default: {
                // special check
                if (currentToken.spelling() == null || currentToken.spelling().isEmpty()) {
                    CLIErrorHandler.displayUsageAndExit(0);
                }

                final String mostProbableCommand = UtilServiceImpl.INSTANCE.findLevenshteinMatchCommand(currentToken.spelling());
                if (mostProbableCommand == null) {
                    CLIErrorHandler.errorAndExit("Unknown command \"%s\". Try `garvel list` to see the list of possible command names\n",
                            currentToken.spelling());
                } else {
                    CLIErrorHandler.errorAndExit("Unknown command \"%s\".\nDid you mean \"%s\"?\nTry `garvel list` to see the list of possible command names\n",
                            currentToken.spelling(), mostProbableCommand);
                }
            }
        }

        return new CommandNameAst(parseCommandIdentifier());
    }

    /**
     * CommandIdentifier ::= "help" | "list" | "version" | "new" | "build" | "clean" | "run" | "test"
     *
     * @return
     */
    private Identifier parseCommandIdentifier() {
        Identifier commandName = null;

        switch (currentToken.kind()) {
            case HELP:
            case LIST:
            case VERSION:
            case INSTALL:
            case UNINSTALL:
            case NEW:
            case BUILD:
            case CLEAN:
            case RUN:
            case DEP:
            case TEST: {
                commandName = new Identifier(currentToken.spelling());
                currentToken = scanner.scan();
            }
            break;

            default:
                CLIErrorHandler.errorAndExit("Error: %s is not a valid argument for the `help` command\nRun `garvel list` to see " +
                        " the valid list of command names", currentToken.spelling());
        }

        return commandName;
    }

    /**
     * Primitive rule - convert the current token's spelling
     * into an Identifier after due error-checking.
     *
     * @return
     */
    private Identifier parseIdentifier() throws CLIException {
        if (currentToken.kind() != IDENTIFIER) {
            throw new CLIException(String.format("CLI Parser Error at col %d. Expected to parse an Identifier, found token of kind %s",
                    currentToken.column(), currentToken.kind()));
        }
        final Identifier id = new Identifier(currentToken.spelling());
        currentToken = scanner.scan();

        return id;
    }

    /**
     * Entry-point for the Garvel CLI.
     *
     * @param args
     * @return the program AST
     */
    @Override
    public Program parse(final String[] args) {
        StringBuffer line = new StringBuffer();

        for (String arg : args) {
            line.append(arg);
            line.append(SPACE);
        }

        scanner = new CLIScanner(line.toString());
        currentToken = scanner.scan();

        final Program program = parseProgram();
        try {
            accept(EOT);
        } catch (CLIException e) {
            // do nothing here
        }

        return program;
    }
}
