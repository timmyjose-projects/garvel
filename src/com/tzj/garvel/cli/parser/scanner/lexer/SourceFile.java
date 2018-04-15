package com.tzj.garvel.cli.parser.scanner.lexer;

import com.tzj.garvel.cli.exception.CLILexerException;
import com.tzj.garvel.common.parser.CharWrapper;

import java.util.ArrayList;
import java.util.List;

import static com.tzj.garvel.common.parser.GarvelConstants.EOI;
import static com.tzj.garvel.common.parser.GarvelConstants.EOL;

public class SourceFile {
    private String line;
    private List<CharWrapper> stream;

    public SourceFile(final String line) {
        this.line = line;
        fillStream();
    }

    private void fillStream() {
        if (line == null || line.isEmpty()) {
            throw new CLILexerException("CLI Lexer Error: No string provided for lexing");
        }
        stream = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            stream.add(new CharWrapper(line.charAt(i), -1, i + 1));
        }
        stream.add(new CharWrapper(EOL, -1, -1));
        stream.add(new CharWrapper(EOI, -1, -1));
    }

    public List<CharWrapper> getStream() {
        return stream;
    }
}
