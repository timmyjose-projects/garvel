package com.tzj.garvel.cli.api.parser;

import com.tzj.garvel.cli.api.parser.ast.Program;

public interface CLIParser {
    Program parse(final String[] args);
}
