package com.tzj.garvel.core.parser.json;

import com.tzj.garvel.core.parser.common.Lexer;
import com.tzj.garvel.core.parser.exception.LexerException;
import com.tzj.garvel.core.parser.exception.ScannerException;

import java.util.List;

public class Scanner {
    private String filename;
    private Lexer lexer;

    private List<JsonToken> tokens;
    private int idx;

    private JsonTokenType currentKind;
    private StringBuffer currentSpelling;
    private JsonToken currentToken;

    public Scanner(final String filename) throws ScannerException {
        this.filename = filename;
        try {
            this.lexer = new Lexer(filename);
        } catch (LexerException e) {
            throw new ScannerException(String.format("Error while creating a lexer for %s", filename));
        }
    }

    private JsonToken nextToken() {
        idx++;
        return tokens.get(idx - 1);
    }

    private boolean hasMoreTokens() {
        return idx < tokens.size();
    }

    public JsonToken scan() throws ScannerException {
        if (!hasMoreTokens()) {
            throw new ScannerException(String.format("No more tokens in the token stream while scanning %s", filename));
        }

        return nextToken();
    }
}
