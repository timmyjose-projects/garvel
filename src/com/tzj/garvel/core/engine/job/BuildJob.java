package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.result.BuildCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;

public class BuildJob implements Job<BuildCommandResult> {
    public BuildJob() {
    }

    /**
     * 1. Populate the Garvel.gl cache.
     * 2. If all the dependencies (with correct versions) are already in situ, then move ahead.
     * Otherwise, reload the required dependencies.
     * 3. Invoke the JavaCompiler (and since `javac` itself does parallelisation, we can skip this
     * unless proven otherwise) and compile the project with the proper classpath.
     * 4.Generate the project JAR
     *
     * @return
     * @throws JobException
     */
    @Override
    public BuildCommandResult call() throws JobException {
        return null;
    }
}
