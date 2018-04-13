package com.tzj.garvel.core.parser;

import com.tzj.garvel.core.parser.json.JsonParserImpl;
import com.tzj.garvel.core.parser.semver.SemverParserImpl;
import com.tzj.garvel.core.parser.spi.JsonParser;
import com.tzj.garvel.core.parser.spi.ParserService;
import com.tzj.garvel.core.parser.spi.SemverParser;

public class ParserServiceImpl implements ParserService {
    @Override
    public JsonParser getJsonParser() {
        return JsonParserImpl.INSTANCE;
    }

    @Override
    public SemverParser getSemVerParser() {
        return SemverParserImpl.INSTANCE;
    }
}
