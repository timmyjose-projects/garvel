package com.tzj.garvel.core.parser.api;

import com.tzj.garvel.core.parser.api.ast.json.JsonObject;
import com.tzj.garvel.core.parser.exception.JsonParserException;

public interface JsonParser {
    JsonObject parse() throws JsonParserException;
}
