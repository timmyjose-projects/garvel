package com.tzj.garvel.core.parser;

import com.tzj.garvel.core.parser.api.TOMLParser;
import com.tzj.garvel.core.parser.semver.SemverParserImpl;
import com.tzj.garvel.core.parser.api.ParserService;
import com.tzj.garvel.core.parser.api.SemverParser;
import com.tzj.garvel.core.parser.toml.TOMLParserImpl;

public enum ParserServiceImpl implements ParserService {
    INSTANCE;

    @Override
    public SemverParser getSemVerParser(final String semverString) {
        return new SemverParserImpl(semverString);
    }

    @Override
    public TOMLParser getTOMLParser(final String filename) {
        return new TOMLParserImpl(filename);
    }
}
