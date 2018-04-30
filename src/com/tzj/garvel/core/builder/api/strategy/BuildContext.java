package com.tzj.garvel.core.builder.api.strategy;

import com.tzj.garvel.core.builder.api.exception.BuildException;

import java.nio.file.Path;

/**
 * This Context objects dispatches the operation to the current
 * strategy class.
 */
public class BuildContext {
    private BuildStrategy strategy;

    public BuildContext(final BuildStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(final BuildStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Execute the strategy.
     *
     * @param classPathString
     * @throws BuildException
     */
    public Path executeStrategy(final String classPathString) throws BuildException {
        return strategy.execute(classPathString);
    }
}
