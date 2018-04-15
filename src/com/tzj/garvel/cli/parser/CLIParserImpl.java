package com.tzj.garvel.cli.parser;

import com.tzj.garvel.cli.api.parser.CLIParser;
import com.tzj.garvel.cli.api.parser.ast.CLIAst;
import com.tzj.garvel.cli.api.parser.scanner.CLIToken;
import com.tzj.garvel.cli.parser.scanner.CLIScanner;

public enum CLIParserImpl implements CLIParser {
    INSTANCE;

    private CLIScanner scanner;
    private CLIToken currentToken;

    @Override
    public CLIAst parse(final String[] args) {
        return null;
    }
}
