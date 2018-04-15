package com.tzj.garvel.cli.parser.scanner.lexer;

import com.tzj.garvel.common.parser.CharWrapper;

import java.util.List;

public class CLILexer {
    private List<CharWrapper> stream;
    private int idx;

    public CLILexer(final String line) {
        this.stream = (new SourceFile(line)).getStream();
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
