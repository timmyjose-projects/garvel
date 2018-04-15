package com.tzj.garvel.cli.parser.scanner;

import com.tzj.garvel.cli.api.parser.scanner.CLIToken;
import com.tzj.garvel.cli.api.parser.scanner.CLITokenType;
import com.tzj.garvel.cli.exception.CLIScannerException;
import com.tzj.garvel.cli.parser.scanner.lexer.CLILexer;
import com.tzj.garvel.common.parser.CharWrapper;

import java.util.ArrayList;
import java.util.List;

import static com.tzj.garvel.common.parser.GarvelConstants.EOI;
import static com.tzj.garvel.common.parser.GarvelConstants.EOL;
import static com.tzj.garvel.common.parser.GarvelConstants.SPACE;

public class CLIScanner {
    private CLILexer lexer;

    private List<CLIToken> tokens;
    private int idx;

    private StringBuffer currentSpelling;
    private CLITokenType currentKind;
    private CharWrapper currentChar;
    private int currentColumn;

    public CLIScanner(final String line) {
        this.lexer = new CLILexer(line);
        this.tokens = new ArrayList<>();
        this.idx = 0;
        this.currentColumn = -1;

        scanAll();
    }

    // test
    public static void main(String[] args) {
        final String text = "garvel --verbose --vcs git new foo";

        CLIScanner scanner = new CLIScanner(text);
        while (scanner.hasMoreTokens()) {
            System.out.println(scanner.scan());
        }
    }

    /**
     * Unconditionally skip the current character
     * in the character stream.
     */
    private void skipIt() {
        currentChar = lexer.nextCharacter();
    }

    private void skip(final char expectedChar) {
        if (currentChar.c() != expectedChar) {
            // error - replace RTE with meaningul error handling

            throw new RuntimeException(String.format("CLI Scanner Error at col %d. Expected to skip %c, found %c",
                    currentChar.column(), expectedChar, currentChar.c()));
        }

        currentChar = lexer.nextCharacter();
    }

    /**
     * Unconditonally append the current char into
     * the current token's spelling buffer.
     */
    private void takeIt() {
        currentSpelling.append(currentChar.c());
        currentChar = lexer.nextCharacter();
    }

    private void take(final char expectedChar) {
        if (currentChar.c() != expectedChar) {
            // error
            throw new RuntimeException(String.format("CLI Scanner Error at col %d. Expected to take %c, found %c",
                    currentChar.column(), expectedChar, currentChar.c()));
        }

        currentChar = lexer.nextCharacter();
    }

    /**
     * Skip all whitespace in the character stream
     */
    private void scanSeparator() {
        switch (currentChar.c()) {
            case SPACE:
            case EOL: {
                skipIt();

                while (currentChar.c() == SPACE || currentChar.c() == EOL) {
                    skipIt();
                }
            }
            break;
        }
    }

    /**
     * Return the token type of the current
     * token being scanned.
     *
     * @return
     */
    private CLITokenType scanToken() {
        CLITokenType kind = null;
        currentColumn = currentChar.column();

        switch (currentChar.c()) {
            case '-': {
                takeIt();

                if (currentChar.c() == '-') {
                    takeIt();
                    while (isLetter(currentChar.c())) {
                        takeIt();
                    }
                } else {
                    takeIt();
                }

                kind = CLITokenType.IDENTIFIER;
            }
            break;


            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '_': {
                takeIt();

                while (isLetter(currentChar.c()) || isDigit(currentChar.c())) {
                    takeIt();
                }

                kind = CLITokenType.IDENTIFIER;
            }
            break;

            case EOI: {
                kind = CLITokenType.EOT;
            }
            break;

            default: {
                // error
                throw new RuntimeException(String.format("CLI SCanner Error: at col %d, %c cannot start a valid token",
                        currentChar.column(), currentChar.c()));
            }
        }

        return kind;
    }

    private boolean isLetter(final char c) {
        switch (c) {
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '_':
            case '-':
            case '/':
                return true;

            default:
                return false;
        }
    }

    private boolean isDigit(final char c) {
        switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;

            default:
                return false;
        }
    }

    /**
     * Construct the token list.
     */
    private void scanAll() {
        currentChar = lexer.nextCharacter();

        while (lexer.hasMoreCharacters()) {
            while (currentChar.c() == SPACE || currentChar.c() == EOL) {
                scanSeparator();
            }

            currentSpelling = new StringBuffer();
            currentKind = scanToken();
            tokens.add(new CLIToken(currentKind, currentSpelling.toString(), currentColumn));
        }
    }

    private boolean hasMoreTokens() {
        return idx < tokens.size();
    }

    private CLIToken nextToken() {
        idx++;
        return tokens.get(idx - 1);
    }

    public CLIToken scan() {
        if (!hasMoreTokens()) {
            // error
            throw new RuntimeException("CLI Scanenr Error: no more tokens in the token stream!");
        }

        return nextToken();
    }
}
