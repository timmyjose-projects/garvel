package com.tzj.garvel.core.parser.api;

import com.tzj.garvel.core.parser.api.ast.toml.ConfigAst;
import com.tzj.garvel.core.parser.exception.TOMLParserException;
import com.tzj.garvel.core.parser.exception.TOMLScannerException;

public interface TOMLParser {
    ConfigAst parse() throws TOMLParserException;
}
