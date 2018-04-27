package com.tzj.garvel.core.dep.api.parser;

import com.tzj.garvel.core.dep.parser.metadata.DepMetadataParser;
import com.tzj.garvel.core.dep.parser.pom.DepPOMParser;

public class DepParserFactory {
    private DepParserFactory() {
    }

    public static DepParser getParser(final DepParserKind kind, final String url) {
        DepParser parser = null;

        switch (kind) {
            case METADATA:
                parser = new DepMetadataParser(url);
                break;
            case POM:
                parser = new DepPOMParser(url);
                break;
        }

        return parser;
    }
}
