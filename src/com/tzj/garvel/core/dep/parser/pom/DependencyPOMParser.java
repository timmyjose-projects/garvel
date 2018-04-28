package com.tzj.garvel.core.dep.parser.pom;

import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.parser.Dependencies;
import com.tzj.garvel.core.dep.api.parser.DependencyParser;
import com.tzj.garvel.core.dep.api.parser.Versions;

public class DependencyPOMParser implements DependencyParser {
    private String pomUrl;
    private Dependencies dependencies;

    public DependencyPOMParser(final String pomUrl) {
        this.pomUrl = pomUrl;
    }

    @Override
    public void parse() throws DependencyManagerException {

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
