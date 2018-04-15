package com.tzj.garvel.core.parser.semver;

import com.tzj.garvel.common.parser.CharWrapper;

import java.util.ArrayList;
import java.util.List;

import static com.tzj.garvel.common.parser.GarvelConstants.EOI;
import static com.tzj.garvel.common.parser.GarvelConstants.EOL;

public class SemverLexer {
    private List<CharWrapper> stream;
    private int idx;

    public SemverLexer(final String semverString) {
        this.stream = new ArrayList<>();
        this.idx = 0;
        fillStream(semverString);
    }

    private void fillStream(final String semverString) {
        stream = new ArrayList<>();

        for (int i = 0; i < semverString.length(); i++) {
            stream.add(new CharWrapper(semverString.charAt(i), -1, i + 1));
        }
        stream.add(new CharWrapper(EOL, -1, -1));
        stream.add(new CharWrapper(EOI, -1, -1));
    }

    public boolean hasMoreCharacters() {
        return idx < stream.size();
    }

    public CharWrapper nextCharacter() {
        idx++;
        return stream.get(idx - 1);
    }
}
