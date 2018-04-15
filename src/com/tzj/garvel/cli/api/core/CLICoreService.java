package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.api.parser.ast.CLIAst;

public interface CLICoreService {
    CLICommand getCommand(final CLIAst ast);
}
