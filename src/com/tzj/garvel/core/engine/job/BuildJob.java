package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.result.BuildCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;

public class BuildJob implements Job<BuildCommandResult> {
    public BuildJob() {
    }

    /**
     * 1). Create the `target` directory with `build` and `deps` directories. `build` will hold the build
     * artifacts (class files basically), and the `deps` directory will hold the dependency graph of the current
     * project in binary form.The Dependency Graph will not only help generate the JAR file more quickly, but
     * also help in incremental builds where only the changed source files will be executed (based on checking
     * the timestamp of the class files against the source file).
     * <p>
     * 2). Parse the Garvel.gl file and populate the Core Cache. If there are any changes (new artifact,
     * changed version of existing artifact), then contact Maven Central and then update the Garvel cache at
     * $HOME/.garvel/cache, as well as update the Dependency Graph of the project itself. The downloaded artifacts
     * will be checksummed using both MD5 and SHA1. For now, only HTTP support will be provided, which should be
     * extended to HTTPS support in later versions.
     * <p>
     * 3). Compile the project and create the deliverable JAR file, ${PROJECT_NAME}.jar in the `target` directory.
     * If there are any errors, report them to the user and exit immediately.
     * <p>
     * Note: Since the idea is to bootstrap Garvel to use Garvel itself, special handling must be done for the
     * `Garvel` project name to generate the the wrapper scripts for `garvel`.jar as well. Whether to restrict
     * new project names with the same name will depend on testing and verification.
     *
     * @return
     * @throws JobException
     */
    @Override
    public BuildCommandResult call() throws JobException {
        return null;
    }
}
