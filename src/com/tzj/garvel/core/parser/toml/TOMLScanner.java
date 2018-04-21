package com.tzj.garvel.core.parser.toml;

import com.tzj.garvel.common.parser.CharWrapper;
import com.tzj.garvel.core.parser.exception.LexerException;
import com.tzj.garvel.core.parser.exception.TOMLScannerException;

import java.util.ArrayList;
import java.util.List;

import static com.tzj.garvel.common.parser.GarvelConstants.*;
import static com.tzj.garvel.core.parser.toml.TOMLTokenType.EQUAL;

public class TOMLScanner {
    private TOMLLexer lexer;

    private List<TOMLToken> tokens;
    private int idx;

    private TOMLTokenType currentKind;
    private StringBuffer currentSpelling;
    private CharWrapper currentChar;
    private int currentLine;
    private int currentColumn;

    public TOMLScanner(final String filename) throws LexerException, TOMLScannerException {
        this.lexer = new TOMLLexer(filename);
        this.tokens = new ArrayList<>();
        this.idx = 0;
        this.currentLine = -1;
        this.currentColumn = -1;

        scanAll();
    }

    // test
    public static void main(String[] args) {
        final String filename = System.getProperty("user.dir") + "/src/com/tzj/garvel/common/templates/garvel-bin.gl";
        try {
            TOMLScanner scanner = new TOMLScanner(filename);

            while (scanner.hasMoreTokens()) {
                System.out.println(scanner.scan());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takeIt() {
        currentSpelling.append(currentChar.c());
        currentChar = lexer.nextCharacter();
    }

    private void take(final char expectedChar) throws TOMLScannerException {
        if (currentChar.c() != expectedChar) {
            throw new TOMLScannerException(String.format("line: %d, column: %d, expected to accept %c, found %c",
                    currentChar.line(), currentChar.column(), expectedChar, currentChar.c()));
        }

        currentSpelling.append(currentChar.c());
        currentChar = lexer.nextCharacter();
    }

    private void skipIt() {
        currentChar = lexer.nextCharacter();
    }

    private void skip(final char expectedChar) throws TOMLScannerException {
        if (currentChar.c() != expectedChar) {
            throw new TOMLScannerException(String.format("line: %d, col: %d, expected to skip %c, found %c",
                    currentChar.line(), currentChar.column(), expectedChar, currentChar.c()));
        }

        currentChar = lexer.nextCharacter();
    }

    private void scanAll() throws TOMLScannerException {
        currentChar = lexer.nextCharacter();

        while (lexer.hasMoreCharacters()) {
            while (currentChar.c() == OCTOTHORPE || currentChar.c() == SPACE || currentChar.c() == EOL) {
                scanSeparator();
            }

            currentSpelling = new StringBuffer();
            currentKind = scanToken();
            tokens.add(new TOMLToken(currentKind, currentSpelling.toString(), currentLine, currentColumn));
        }
    }

    private TOMLTokenType scanToken() throws TOMLScannerException {
        TOMLTokenType kind = null;
        currentLine = currentChar.line();
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
            case 'Z':
            case '_':
            case '/':
            case '.': {
                takeIt();

                while (isLetter(currentChar.c()) || isDigit(currentChar.c())) {
                    takeIt();
                }

                kind = TOMLTokenType.IDENTIFIER;
            }
            break;

            case '"': {
                takeIt();

                while (isLetter(currentChar.c()) || isDigit(currentChar.c())) {
                    takeIt();
                }
                take(DOUBLE_QUOTE);

                kind = TOMLTokenType.IDENTIFIER;
            }
            break;

            case '[': {
                takeIt();
                kind = TOMLTokenType.LEFT_BRACKET;
            }
            break;

            case ']': {
                takeIt();
                kind = TOMLTokenType.RIGHT_BRACKET;
            }
            break;

            case ',': {
                takeIt();
                kind = TOMLTokenType.COMMA;
            }
            break;

            case '=': {
                takeIt();
                kind = EQUAL;
            }
            break;

            case EOI: {
                kind = TOMLTokenType.EOT;
            }
            break;

            default:
                throw new TOMLScannerException(String.format("line: %d, col: %d, %c cannot start a valid token",
                        currentChar.line(), currentChar.column(), currentChar.c()));
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
            case 'Z':
            case '_':
            case '/':
            case ':':
            case ';':
            case '-':
            case '+':
            case '.':
            case '@':
            case SPACE:
            case COMMA:
            case '!':
                return true;
            default:
                return false;

        }
    }

    private void scanSeparator() throws TOMLScannerException {
        switch (currentChar.c()) {
            case OCTOTHORPE: {
                skipIt();
                while (isGraphic(currentChar.c())) {
                    skipIt();
                }
                skip(EOL);
            }
            break;
            case SPACE:
            case EOL:
                skipIt();
                break;
        }
    }

    private boolean isGraphic(final char c) {
        switch (c) {
            case EOL:
                return false;
            default:
                return true;
        }
    }

    private boolean hasMoreTokens() {
        return idx < tokens.size();
    }

    private TOMLToken nextToken() {
        idx++;
        return tokens.get(idx - 1);
    }

    public TOMLToken scan() throws TOMLScannerException {
        if (!hasMoreTokens()) {
            throw new TOMLScannerException("No more tokens!");
        }

        return nextToken();
    }
}
