package com.tzj.garvel.core.parser.spi;

public interface ParserService {
    JsonParser getJsonParser();

    SemverParser getSemVerParser();
}
