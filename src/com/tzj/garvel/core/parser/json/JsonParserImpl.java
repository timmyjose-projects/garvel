package com.tzj.garvel.core.parser.json;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;
import com.tzj.garvel.core.parser.exception.JsonParserException;
import com.tzj.garvel.core.parser.spi.JsonParser;

import java.util.List;

public enum JsonParserImpl implements JsonParser {
    INSTANCE;

    @Override
    public void parse(final String filename) throws JsonParserException {

    }

    @Override
    public void parse(final List<String> filenames) throws JsonParserException {

    }
}
