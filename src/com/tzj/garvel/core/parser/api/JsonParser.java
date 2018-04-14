package com.tzj.garvel.core.parser.api;

import com.tzj.garvel.core.parser.exception.JsonParserException;

public interface JsonParser {
    void parse(final String filename) throws JsonParserException;
}
