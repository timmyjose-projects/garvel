package com.tzj.garvel.core.parser.json;

import com.tzj.garvel.core.parser.common.CharWrapper;

import java.util.List;

public class JsonLexer {
    private String filename;
    private List<CharWrapper> charStream;
    private int idx;

    public JsonLexer(final String filename) {
        this.filename = filename;
        this.charStream = (new JsonSourceFile(filename)).getStream();
    }

    public boolean hasMoreCharacters() {
        return idx < charStream.size();
    }

    public CharWrapper nextCharacter() {
        idx++;
        return charStream.get(idx - 1);
    }
}
