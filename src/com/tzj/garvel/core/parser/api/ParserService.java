package com.tzj.garvel.core.parser.api;

import com.tzj.garvel.core.parser.exception.TOMLParserException;

public interface ParserService {
    SemverParser getSemVerParser(final String semverString);

    TOMLParser getTOMLParser(final String filename) throws TOMLParserException;
}
