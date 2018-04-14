package com.tzj.garvel.core.parser.api;

import com.tzj.garvel.core.parser.api.ast.JsonObject;
import com.tzj.garvel.core.parser.exception.JsonParserException;

import java.util.List;

public interface JsonParser {
    JsonObject parse() throws JsonParserException;
}
