package com.tzj.garvel.core.parser.semver;

import com.tzj.garvel.common.parser.CharWrapper;
import com.tzj.garvel.core.parser.exception.LexerException;
import com.tzj.garvel.core.parser.exception.SemverScannerException;

import java.util.ArrayList;
import java.util.List;

import static com.tzj.garvel.common.parser.GarvelConstants.EOI;
import static com.tzj.garvel.common.parser.GarvelConstants.EOL;
import static com.tzj.garvel.common.parser.GarvelConstants.SPACE;

public class SemverScanner {
    private String filename;
    private SemverLexer lexer;

    private List<SemverToken> tokens;
    private int idx;

    private SemverTokenType currentKind;
    private StringBuffer currentSpelling;

    private CharWrapper currentChar;
    private int currentColumn;

    public SemverScanner(final String filename) throws SemverScannerException {
        this.filename = filename;
        this.lexer = new SemverLexer(filename);
        this.tokens = new ArrayList<>();
        scanAll();
    }

    // test
    public static void main(String[] args) {
        final String semver = "4.12-beta-3";

        SemverScanner scanner = new SemverScanner(semver);
        while (scanner.hasMoreTokens()) {
            System.out.println(scanner.scan());
        }
    }

    /**
     * Unconditionally skip to the next character in the character stream.
     */
    private void skipIt() {
        currentChar = lexer.nextCharacter();
    }

    /**
     * Unconditionally append the current character to currentSpelling.
     */
    private void takeIt() {
        currentSpelling.append(currentChar.c());
        currentChar = lexer.nextCharacter();
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
            tokens.add(new SemverToken(currentKind, currentSpelling.toString(), currentColumn));
        }
    }

    private SemverTokenType scanToken() {
        SemverTokenType kind = null;
        currentColumn = currentChar.column();

        switch (currentChar.c()) {
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
            case 'Z': {
                takeIt();

                while (isLetter(currentChar.c()) || isDigit(currentChar.c())) {
                    takeIt();
                }

                kind = SemverTokenType.IDENTIFIER;
            }
            break;

            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                takeIt();

                while (isDigit(currentChar.c())) {
                    takeIt();
                }

                kind = SemverTokenType.INTLITERAL;
            }
            break;

            case '.': {
                takeIt();
                kind = SemverTokenType.PERIOD;
            }
            break;

            case '-': {
                takeIt();
                kind = SemverTokenType.DASH;
            }
            break;

            case '+': {
                takeIt();
                kind = SemverTokenType.PLUS;
            }
            break;

            case EOI: {
                kind = SemverTokenType.EOT;
            }
            break;

            default:
                throw new SemverScannerException(String.format("Semver Scanner Error at column %d. %c cannot start a valid token",
                        currentChar.column(), currentChar.c()));
        }

        return kind;
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
            case '-':
                return true;

            default:
                return false;
        }
    }

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

    private boolean hasMoreTokens() {
        return idx < tokens.size();
    }

    private SemverToken nextToken() {
        idx++;
        return tokens.get(idx - 1);
    }

    public SemverToken scan() throws SemverScannerException {
        if (!hasMoreTokens()) {
            throw new SemverScannerException("No more tokens in the token stream");
        }

        return nextToken();
    }
}
