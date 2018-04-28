package com.tzj.garvel.core.dep.api.parser;

import com.tzj.garvel.core.dep.parser.metadata.DependencyMetadataParser;
import com.tzj.garvel.core.dep.parser.pom.DependencyPOMParser;

public class DependencyParserFactory {
    private DependencyParserFactory() {
    }

    public static DependencyParser getParser(final DependencyParserKind kind, final String url) {
        DependencyParser parser = null;

        switch (kind) {
            case METADATA:
                parser = new DependencyMetadataParser(url);
                break;
            case POM:
                parser = new DependencyPOMParser(url);
                break;
        }

        return parser;
    }
}
