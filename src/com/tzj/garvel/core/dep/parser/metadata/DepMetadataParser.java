package com.tzj.garvel.core.dep.parser.metadata;

import com.tzj.garvel.core.dep.api.parser.DepParser;
import com.tzj.garvel.core.dep.api.parser.Dependencies;
import com.tzj.garvel.core.dep.api.parser.Versions;

public class DepMetadataParser implements DepParser {
    private String url;
    private Versions versions;

    public DepMetadataParser(final String url) {
        this.url = url;
    }

    @Override
    public Versions getVersions() {
        return null;
    }

    public void setVersions(final Versions versions) {
        this.versions = versions;
    }

    @Override
    public Dependencies getDependencies() {
        throw new UnsupportedOperationException("getDependencies is not supported by DepMetadataParser");
    }
}
