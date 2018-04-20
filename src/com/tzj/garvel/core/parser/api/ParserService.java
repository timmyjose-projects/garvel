package com.tzj.garvel.core.parser.api;

public interface ParserService {
    JsonParser getJsonParser(final String filename);

    SemverParser getSemVerParser(final String semverString);

    TOMLParser getTOMLParser(final String filename);
}
