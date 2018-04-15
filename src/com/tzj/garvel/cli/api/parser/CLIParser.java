package com.tzj.garvel.cli.api.parser;

import com.tzj.garvel.cli.api.parser.ast.CLIAst;

public interface CLIParser {
    CLIAst parse(final String[] args);
}
