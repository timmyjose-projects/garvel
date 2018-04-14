package com.tzj.garvel.core.parser.json;

import com.tzj.garvel.core.parser.exception.JsonParserException;
import com.tzj.garvel.core.parser.spi.JsonParser;

import java.util.List;

public enum JsonParserImpl implements JsonParser {
    INSTANCE;

    private JsonScanner scanner;
    private JsonToken currentToken;

    @Override
    public void parse(final String filename) throws JsonParserException {
        
    }
}
