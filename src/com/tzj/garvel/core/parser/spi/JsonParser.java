package com.tzj.garvel.core.parser.spi;

import com.tzj.garvel.core.parser.exception.JsonParserException;

import java.util.List;

public interface JsonParser {
    void parse(final String filename) throws JsonParserException;

    void parse(final List<String> filenames) throws JsonParserException;
}
