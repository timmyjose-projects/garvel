package com.tzj.garvel.core.parser.toml;

import com.tzj.garvel.common.parser.CharWrapper;
import com.tzj.garvel.core.parser.exception.LexerException;

import java.util.List;

public class TOMLLexer {
    private List<CharWrapper> stream;
    private int idx;

    public TOMLLexer(final String filename) throws LexerException {
        this.stream = new TOMLSourceFile(filename).getStream();
        this.idx = 0;
    }

    public boolean hasMoreCharacters() {
        return idx < stream.size();
    }

    public CharWrapper nextCharacter() {
        idx++;
        return stream.get(idx - 1);
    }
}
