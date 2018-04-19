package com.tzj.garvel.cli.parser;

import com.tzj.garvel.cli.api.parser.CLIParser;
import com.tzj.garvel.cli.api.parser.ast.*;
import com.tzj.garvel.cli.api.parser.scanner.CLIToken;
import com.tzj.garvel.cli.api.parser.scanner.CLITokenType;
import com.tzj.garvel.cli.parser.scanner.CLIScanner;

import static com.tzj.garvel.cli.api.parser.scanner.CLITokenType.EOT;
import static com.tzj.garvel.cli.api.parser.scanner.CLITokenType.IDENTIFIER;
import static com.tzj.garvel.common.parser.GarvelConstants.SPACE;

public enum CLIParserImpl implements CLIParser {
    INSTANCE;

    private CLIScanner scanner;
    private CLIToken currentToken;

    // test
    public static void main(String[] args) {
        final String[] input = new String[]{"garvel", "--verbose", "new", "--vcs", "git", "--lib", "foo"};

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
    private void accept(final CLITokenType expectedKind) {
        if (currentToken.kind() != expectedKind) {
            //error
            throw new RuntimeException(String.format("CLI Parser Error at col %d. Expected to accept token of kind %s, found token of kind %s",
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
        accept(CLITokenType.GARVEL);

        Program program = null;

        switch (currentToken.kind()) {
            case VERBOSE: {
                acceptIt();
                final Command command = parseCommand();

                program = new Program(true, false, command);
            }
            break;

            case QUIET: {
                acceptIt();
                final Command command = parseCommand();

                program = new Program(false, true, command);
            }
            break;

            case IDENTIFIER: {
                final Command command = parseCommand();

                // default quiet
                program = new Program(false, true, command);
            }
            break;

            default: {
                // @TODO error-handling with meaningful error messages
                throw new RuntimeException(String.format("CLI Parser Error at col %d. %s cannot start a valid garvel command",
                        currentToken.column(), currentToken.kind()));
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
    private Command parseCommand() {
        Command command = null;

        switch (currentToken.kind()) {
            case HELP: {
                acceptIt();
                final CommandName commandName = parseCommandName();
                command = new HelpCommand();
            }
            break;

            case LIST: {
                acceptIt();
                command = new ListCommand();
            }
            break;

            case VERSION: {
                acceptIt();
                command = new VersionCommand();
            }
            break;

            case NEW: {
                acceptIt();

                switch (currentToken.kind()) {
                    case VCS: {
                        acceptIt();

                        final VCS vcs = parseVCS();

                        switch (currentToken.kind()) {
                            case BIN: {
                                acceptIt();

                                final Path path = parsePath();
                                command = new NewCommand(vcs, true, false, path);
                            }
                            break;

                            case LIB: {
                                acceptIt();

                                final Path path = parsePath();
                                command = new NewCommand(vcs, false, true, path);
                            }
                            break;

                            case IDENTIFIER: {
                                final Path path = parsePath();

                                // default bin
                                command = new NewCommand(vcs, true, false, path);
                            }
                            break;

                            default: {
                                // error
                                throw new RuntimeException(String.format("CLI Parser Error at col %d. Either %s, %s, or a valid PATH  must follow --vcs",
                                        currentToken.column(), CLITokenType.BIN, CLITokenType.LIB));
                            }
                        }
                    }
                    break;

                    case BIN: {
                        acceptIt();

                        final Path path = parsePath();
                        command = new NewCommand(new VCS(new Identifier(VCSType.NONE.toString())), true, false, path);
                    }
                    break;

                    case LIB: {
                        acceptIt();

                        final Path path = parsePath();
                        command = new NewCommand(new VCS(new Identifier(VCSType.NONE.toString())), false, true, path);
                    }
                    break;

                    case IDENTIFIER: {
                        final Path path = parsePath();

                        // bin is default
                        command = new NewCommand(new VCS(new Identifier(VCSType.NONE.toString())), true, false, path);
                    }
                    break;

                    default: {
                        // error
                        throw new RuntimeException(String.format("CLI Parser Error at col %d. Expected %s, %s, or %s, found %s",
                                currentToken.column(), CLITokenType.VCS, CLITokenType.BIN, CLITokenType.LIB, currentToken.kind()));
                    }
                }
            }
            break;

            case BUILD: {
                acceptIt();
                command = new BuildCommand();
            }
            break;

            case CLEAN: {
                acceptIt();
                command = new CleanCommand();
            }
            break;

            case RUN: {
                acceptIt();
                command = new RunCommand();
            }
            break;

            case TEST: {
                acceptIt();
                command = new TestCommand();
            }
            break;

            default: {
                //proper error-handling - also use Utils' Levenshtein distance to provide suggestions.
                throw new RuntimeException(String.format("CLI Parser Error at col %d. %s is not a valid command.",
                        currentToken.column(), currentToken.kind()));
            }
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
    private VCS parseVCS() {
        return new VCS(parseIdentifier());
    }

    /**
     * CommandName ::= Identifier
     *
     * @return
     */
    private CommandName parseCommandName() {
        return new CommandName(parseIdentifier());
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
        accept(EOT);

        return program;
    }
}
