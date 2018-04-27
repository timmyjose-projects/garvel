package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.DepCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.dep.api.exception.RepositoryLoaderException;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoader;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoaderFactory;
import com.tzj.garvel.core.engine.exception.JobException;

import java.util.List;

public class DepJob implements Job<DepCommandResult> {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final boolean showDependencies;

    public DepJob(final String groupId, final String artifactId, final String version, final boolean showDependencies) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
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

        queryDependencyGraph();
        queryLocalCache();
        queryRepos();

        return result;
    }

    /**
     * Query the central repos directly.
     *
     * @throws JobException
     */
    private void queryRepos() throws JobException {
        final RepositoryLoader repoLoader = RepositoryLoaderFactory.getLoader();
        String repoUrl = null;

        try {
            repoUrl = repoLoader.constructArtifactUrl(groupId, artifactId);
        } catch (RepositoryLoaderException e) {
            throw new JobException(e.getErrorString());
        }

        // get the maven metadata file and populate version info


        // if the --show-dependencies flag was supplier, then
        // get the artifact POM (using version) and populate
        // the transitive dependency information.

    }

    /**
     * Query the Garvel cache ($HOME/.garvel/cache), if available.
     *
     * @throws JobException
     */
    private void queryLocalCache() throws JobException {

    }

    /**
     * Query the project's Dependency Graph, if available.
     *
     * @throws JobException
     */
    private void queryDependencyGraph() throws JobException {

    }
}
