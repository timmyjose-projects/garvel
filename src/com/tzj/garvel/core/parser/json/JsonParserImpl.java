package com.tzj.garvel.core.parser.json;

import com.tzj.garvel.core.parser.exception.JsonParserException;
import com.tzj.garvel.core.parser.api.JsonParser;

public enum JsonParserImpl implements JsonParser {
    INSTANCE;

    private JsonScanner scanner;
    private JsonToken currentToken;

    @Override
    public void parse(final String filename) throws JsonParserException {

    }
}
