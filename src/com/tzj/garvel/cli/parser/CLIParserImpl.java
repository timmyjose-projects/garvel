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

import static com.tzj.garvel.cli.api.parser.scanner.CLITokenType.EOT;
import static com.tzj.garvel.cli.api.parser.scanner.CLITokenType.IDENTIFIER;
import static com.tzj.garvel.common.parser.GarvelConstants.SPACE;

public enum CLIParserImpl implements CLIParser {
    INSTANCE;

    private CLIScanner scanner;
    private CLIToken currentToken;

    // test
    public static void main(String[] args) {
        //final String[] input = new String[]{"garvel", "--verbose", "new", "--vcs", "git", "--lib", "foo"};
        //final String[] input = new String[]{"garvel", "help", "new"};
        final String[] input = new String[]{"garvel", "run"};

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
            case NEW:
            case BUILD:
            case CLEAN:
            case RUN:
            case TEST: {
                final CommandAst command = parseCommand();

                // default quiet
                program = new Program(false, true, command);
            }
            break;

            default: {
                final String bestMatch = ModuleLoader.INSTANCE.getUtils().findLevenshteinMatch(currentToken.spelling());

                if (bestMatch != null) {
                    CLIErrorHandler.errorAndExit(String.format("\"%s\" is not a valid command.\n Did you mean \"%s\"?",
                            currentToken.spelling(), ModuleLoader.INSTANCE.getUtils().findLevenshteinMatch(currentToken.spelling())));
                } else {
                    CLIErrorHandler.errorAndExit(String.format("%s is not a valid command.",
                            currentToken.spelling()));
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

            case NEW: {
                acceptIt();

                switch (currentToken.kind()) {
                    case VCS: {
                        acceptIt();

                        final VCSAst vcs = parseVCS();

                        switch (currentToken.kind()) {
                            case BIN: {
                                acceptIt();

                                final Path path = parsePath();
                                command = new NewCommandAst(vcs, true, false, path);
                            }
                            break;

                            case LIB: {
                                acceptIt();

                                final Path path = parsePath();
                                command = new NewCommandAst(vcs, false, true, path);
                            }
                            break;

                            case IDENTIFIER: {
                                final Path path = parsePath();

                                // default bin
                                command = new NewCommandAst(vcs, true, false, path);
                            }
                            break;

                            default: {
                                CLIErrorHandler.errorAndExit(String.format("Error: Either %s, %s, or a valid PATH  must follow --vcs",
                                        CLITokenType.BIN, CLITokenType.LIB));
                            }
                        }
                    }
                    break;

                    case BIN: {
                        acceptIt();

                        final Path path = parsePath();
                        command = new NewCommandAst(new VCSAst(new Identifier(VCSType.NONE.toString())), true, false, path);
                    }
                    break;

                    case LIB: {
                        acceptIt();

                        final Path path = parsePath();
                        command = new NewCommandAst(new VCSAst(new Identifier(VCSType.NONE.toString())), false, true, path);
                    }
                    break;

                    case IDENTIFIER: {
                        final Path path = parsePath();

                        // bin is default
                        command = new NewCommandAst(new VCSAst(new Identifier(VCSType.NONE.toString())), true, false, path);
                    }
                    break;

                    default: {
                        CLIErrorHandler.errorAndExit(String.format("Error: Either %s, %s, or %s must follow the `new` command",
                                CLITokenType.VCS, CLITokenType.BIN, CLITokenType.LIB));
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
                command = new RunCommandAst();
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

    /**
     * Path ::= Identifier
     *
     * @return
     */
    private Path parsePath() {
        return new Path(parseIdentifier());
    }

    /**
     * VCS ::= Identifier
     *
     * @return
     */
    private VCSAst parseVCS() {
        return new VCSAst(parseIdentifier());
    }

    /**
     * CommandName ::= Identifier
     *
     * @return
     */
    private CommandNameAst parseCommandName() {
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
            case NEW:
            case BUILD:
            case CLEAN:
            case RUN:
            case TEST: {
                commandName = new Identifier(currentToken.spelling());
                currentToken = scanner.scan();
            }
            break;

            default:
                CLIErrorHandler.errorAndExit(String.format("Error: %s is not a valid argument for the `help` command\nRun `garvel list` to see " +
                        " the valid list of command names", currentToken.spelling()));
        }

        return commandName;
    }

    /**
     * Primitive rule - convert the current token's spelling
     * into an Identifier after due error-checking.
     *
     * @return
     */
    private Identifier parseIdentifier() {
        if (currentToken.kind() != IDENTIFIER) {
            // error-handling
            throw new RuntimeException(String.format("CLI Parser Error at col %d. Expected to parse an identifier, found token of kind %s",
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
