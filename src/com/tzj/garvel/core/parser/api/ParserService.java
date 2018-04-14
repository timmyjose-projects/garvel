package com.tzj.garvel.core.parser.api;

public interface ParserService {
    JsonParser getJsonParser(final String filename);

    SemverParser getSemVerParser();
}
