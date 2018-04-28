package com.tzj.garvel.core.dep.api.parser;

import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;

public interface DependencyParser {
    void parse() throws DependencyManagerException;

    Versions getVersions();

    Dependencies getDependencies();
}
