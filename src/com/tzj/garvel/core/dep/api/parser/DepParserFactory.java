package com.tzj.garvel.core.dep.api.parser;

import com.tzj.garvel.core.dep.parser.metadata.DepMetadataParser;
import com.tzj.garvel.core.dep.parser.pom.DepPOMParser;

public class DepParserFactory {
    private DepParserFactory() {
    }

    public static DepParser getParser(final DepParserKind kind) {
        DepParser parser = null;

        switch (kind) {
            case METADATA:
                parser = new DepMetadataParser();
                break;
            case POM:
                parser = new DepPOMParser();
                break;
        }

        return parser;
    }
}
