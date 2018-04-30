package com.tzj.garvel.core.builder.api.strategy;

import com.tzj.garvel.core.builder.api.exception.BuildException;

import java.nio.file.Path;

public interface BuildStrategy {
    Path execute(final String classPathString) throws BuildException;
}
