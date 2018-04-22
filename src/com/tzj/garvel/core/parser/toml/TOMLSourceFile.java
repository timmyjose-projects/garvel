package com.tzj.garvel.core.parser.toml;

import com.tzj.garvel.common.parser.CharWrapper;
import com.tzj.garvel.core.parser.exception.LexerException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tzj.garvel.common.parser.GarvelConstants.EOI;
import static com.tzj.garvel.common.parser.GarvelConstants.EOL;

public class TOMLSourceFile {
    private String filename;
    private List<CharWrapper> stream;

    public TOMLSourceFile(final String filename) throws LexerException {
        this.filename = filename;
        this.stream = new ArrayList<>();
        fillStream();
    }

    // test
    public static void main(String[] args) throws LexerException {
        final String filename = System.getProperty("user.dir") + "/src/com/tzj/garvel/common/templates/GarvelTemplate.gl";

        TOMLSourceFile srcFile = new TOMLSourceFile(filename);
        List<CharWrapper> stream = srcFile.getStream();

        for (CharWrapper c : stream) {
            System.out.printf("%c", c.c());
        }

    }

    private void fillStream() throws LexerException {
        int linum = 1;
        String line = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    stream.add(new CharWrapper(line.charAt(i), linum, i + 1));
                }
                stream.add(new CharWrapper(EOL, linum, -1));
                linum++;
            }
            stream.add(new CharWrapper(EOI, -1, -1));
        } catch (FileNotFoundException e1) {
            throw new LexerException(String.format("TOML Lexer error: source file %s does not exist", filename));

        } catch (IOException e1) {
            throw new LexerException(String.format("TOML Lexer error: while trying to read source file %s", filename));
        }
    }

    public List<CharWrapper> getStream() {
        return stream;
    }
}
