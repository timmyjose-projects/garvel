package com.tzj.garvel.core.parser.api;

import com.tzj.garvel.core.parser.exception.JsonParserException;

import java.util.List;

public interface JsonParser {
    List<JsonObject> parse(final String filename) throws JsonParserException;
}
