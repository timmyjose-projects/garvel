package com.tzj.garvel.core.parser.common;

import com.tzj.garvel.core.parser.exception.LexerException;

import java.util.List;

public class Lexer {
    private String filename;
    private List<CharWrapper> charStream;
    private int idx;

    public Lexer(final String filename) throws LexerException {
        this.filename = filename;
        this.charStream = (new SourceFile(filename)).getStream();
    }

    public boolean hasMoreCharacters() {
        return idx < charStream.size();
    }

    public CharWrapper nextCharacter() {
        idx++;
        return charStream.get(idx - 1);
    }
}
