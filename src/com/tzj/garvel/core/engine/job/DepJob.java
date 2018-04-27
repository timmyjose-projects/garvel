package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.DepCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;

public class DepJob implements Job<DepCommandResult> {
    private final String dependencyName;
    private final boolean showDependencies;

    public DepJob(final String dependencyName, final boolean showDependencies) {
        this.dependencyName = dependencyName;
        this.showDependencies = showDependencies;
    }

    /**
     * 1. Given the Maven Coordinates, display the available versions. To this end, the local cache will be queried
     * first, and if not found, then Maven Central will be contacted for ths information.
     * <p>
     * 2. If the `--show-dependencies` flag is supplied, then the transitive dependencies of the artifact will
     * also be displayed in a suitably formatted manner.
     *
     * @return
     * @throws Exception
     */
    @Override
    public DepCommandResult call() throws Exception {
        DepCommandResult result = null;

        validateMavenCoordinates();
        queryDependencyGraph();
        queryLocalCache();
        queryRepos();

        return result;
    }

    private void queryRepos() throws JobException {

    }

    private void queryLocalCache() throws JobException {

    }

    private void queryDependencyGraph() throws JobException {

    }

    private void validateMavenCoordinates() throws JobException {

    }
}
