package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.DepCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.exception.RepositoryLoaderException;
import com.tzj.garvel.core.dep.api.parser.DependencyParser;
import com.tzj.garvel.core.dep.api.parser.DependencyParserFactory;
import com.tzj.garvel.core.dep.api.parser.DependencyParserKind;
import com.tzj.garvel.core.dep.api.parser.Versions;
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
    public DepCommandResult call() throws JobException {
        DepCommandResult result = new DepCommandResult();

        if (queryDependencyGraph(result)) {
            return result;
        }

        if (queryLocalCache(result)) {
            return result;
        }

        queryRepos(result);

        return result;
    }

    /**
     * Query the central repos directly.
     *
     * @param result
     * @throws JobException
     */
    private void queryRepos(final DepCommandResult result) throws JobException {
        final RepositoryLoader repoLoader = RepositoryLoaderFactory.getLoader();
        String repoUrl = null;

        try {
            repoUrl = repoLoader.constructArtifactUrl(groupId, artifactId);
        } catch (RepositoryLoaderException e) {
            throw new JobException(e.getErrorString());
        }

        // get the maven metadata file and populate version info
        result.setVersions(getVersionsForDependency(repoUrl));

        // if the --show-dependencies flag was supplier, then
        // get the artifact POM (using version) and populate
        // the transitive dependency information.
        final String deps = getTransitiveDependencies(repoUrl);
        if (deps != null) {
            result.setDependenciesInformationAvailable(true);
            result.setDependencyGraphString(deps);
        }
    }

    /**
     * Retrieve the transitive dependencies of the artifact.
     *
     * @param repoUrl
     * @return
     */
    private String getTransitiveDependencies(final String repoUrl) {
        return null;
    }

    /**
     * Retrieve the versions information from the maven-metadata.xml file
     * at the proper url.
     *
     * @param repoUrl
     * @return
     */
    private String getVersionsForDependency(final String repoUrl) throws JobException {
        final DependencyParser parser = DependencyParserFactory.getParser(DependencyParserKind.METADATA, repoUrl);
        try {
            parser.parse();
        } catch (DependencyManagerException e) {
            throw new JobException(e.getErrorString());
        }

        final StringBuffer sb = new StringBuffer();
        final Versions versions = parser.getVersions();

        if (versions.getLatestVersion() != null) {
            sb.append(String.format("Latest version: %s\n", versions.getLatestVersion()));
        }

        if (versions.getReleaseVersion() != null) {
            sb.append(String.format("Release version: %s\n", versions.getReleaseVersion()));
        }

        final List<String> availableVersions = versions.getAvailableVersions();
        if (availableVersions != null && !availableVersions.isEmpty()) {
            sb.append("Available versions:\n");

            for (String version : availableVersions) {
                sb.append(String.format("\t%s\n", version));
            }
        }

        return sb.toString();
    }

    /**
     * Query the Garvel cache ($HOME/.garvel/cache), if available.
     *
     * @throws JobException
     * @param result
     */
    private boolean queryLocalCache(final DepCommandResult result) throws JobException {
        return false;
    }

    /**
     * Query the project's Dependency Graph, if available.
     *
     * @throws JobException
     * @param result
     */
    private boolean queryDependencyGraph(final DepCommandResult result) throws JobException {
        return false;
    }
}
