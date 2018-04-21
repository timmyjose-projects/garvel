package com.tzj.garvel.core.parser.api;

public interface ParserService {
    SemverParser getSemVerParser(final String semverString);

    TOMLParser getTOMLParser(final String filename);
}
