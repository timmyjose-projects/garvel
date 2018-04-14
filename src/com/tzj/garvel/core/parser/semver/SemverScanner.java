package com.tzj.garvel.core.parser.semver;

import com.tzj.garvel.core.parser.common.CharWrapper;
import com.tzj.garvel.core.parser.common.Lexer;
import com.tzj.garvel.core.parser.exception.LexerException;
import com.tzj.garvel.core.parser.exception.SemverScannerException;

import java.util.ArrayList;
import java.util.List;

public class SemverScanner {
    private String filename;
    private Lexer lexer;

    private List<SemverToken> tokens;
    private int idx;

    private SemverTokenType currentKind;
    private StringBuffer currentSpelling;
    private CharWrapper currentChar;

    public SemverScanner(final String filename) throws SemverScannerException {
        this.filename = filename;
        try {
            this.lexer = new Lexer(filename);
        } catch (LexerException e) {
            throw new SemverScannerException(String.format("Unable to create lexer for file %s", filename));
        }

        this.tokens = new ArrayList<>();
        scanAll();
    }

    private void skipIt() {

    }

    private void skip(final char expectedChar) {

    }

    private void takeIt() {

    }

    private void scanAll() {

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
