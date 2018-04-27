package com.tzj.garvel.core.dep.parser.pom;

import com.tzj.garvel.core.dep.api.parser.DepParser;
import com.tzj.garvel.core.dep.api.parser.Dependencies;
import com.tzj.garvel.core.dep.api.parser.Versions;

public class DepPOMParser implements DepParser {
    private String pomUrl;
    private Dependencies dependencies;

    public DepPOMParser(final String pomUrl) {
        this.pomUrl = pomUrl;
    }

    @Override
    public Versions getVersions() {
        throw new UnsupportedOperationException("getVersions is not supported by DepPOMParser");
    }

    @Override
    public Dependencies getDependencies() {
        return null;
    }

    public void setDependencies(final Dependencies dependencies) {
        this.dependencies = dependencies;
    }
}
