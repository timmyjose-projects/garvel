package com.tzj.garvel.core.parser.common;

import com.tzj.garvel.common.GarvelConstants;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;
import com.tzj.garvel.core.parser.exception.LexerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SourceFile {
    String filename;
    List<CharWrapper> stream;

    public SourceFile(final String filename) throws LexerException {
        this.filename = filename;
        this.stream = new ArrayList<>();
        fillStream();
    }

    private void fillStream() throws LexerException {
        try {
            BufferedReader reader = CoreModuleLoader.INSTANCE.getFileSystemFramework().newBufferedReader(filename);

            int linum = 1;
            String line = null;
            while ((line = reader.readLine()) != null) {
                int colnum = 1;
                for (int i = 0; i < line.length(); i++) {
                    stream.add(new CharWrapper(line.charAt(i), linum, colnum));
                    colnum++;
                }
                stream.add(new CharWrapper(GarvelConstants.EOL, linum, colnum));
                linum++;
            }
            stream.add(new CharWrapper(GarvelConstants.EOI, -1, -1));
        } catch (FilesystemFrameworkException e) {
            throw new LexerException(String.format("Error while trying to load %s for lexing. Message: %s", e.getLocalizedMessage()));
        } catch (IOException e) {
            throw new LexerException(String.format("Error while trying to lex file %s. Message: %s", e.getLocalizedMessage()));
        }
    }

    public List<CharWrapper> getStream() {
        return stream;
    }
}
