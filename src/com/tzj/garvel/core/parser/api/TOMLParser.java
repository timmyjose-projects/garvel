package com.tzj.garvel.core.parser.api;

import com.tzj.garvel.core.parser.exception.TOMLParserException;

public interface TOMLParser {
    void parse() throws TOMLParserException;
}
