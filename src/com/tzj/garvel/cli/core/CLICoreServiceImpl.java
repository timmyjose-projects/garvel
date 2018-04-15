package com.tzj.garvel.cli.core;

import com.tzj.garvel.cli.api.core.CLICommand;
import com.tzj.garvel.cli.api.core.CLICoreService;
import com.tzj.garvel.cli.api.parser.ast.CLIAst;

public enum CLICoreServiceImpl implements CLICoreService {
    INSTANCE;

    @Override
    public CLICommand getCommand(final CLIAst ast) {
        return null;
    }
}
