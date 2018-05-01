package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.CoreServiceLoader;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.DepCommandParams;
import com.tzj.garvel.common.spi.core.command.result.DepCommandResult;
import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.dep.DependencyGraph;
import com.tzj.garvel.core.dep.api.Artifact;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.exception.DependencyResolverException;
import com.tzj.garvel.core.dep.api.exception.RepositoryLoaderException;
import com.tzj.garvel.core.dep.api.graph.*;
import com.tzj.garvel.core.dep.api.parser.*;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoader;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoaderFactory;
import com.tzj.garvel.core.dep.graph.AdjacencySet;
import com.tzj.garvel.core.dep.graph.Algorithms;
import com.tzj.garvel.core.dep.graph.GraphDisplayCallback;
import com.tzj.garvel.core.dep.graph.GraphIdGenerator;
import com.tzj.garvel.core.engine.exception.JobException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 2. If the `--show-dependencies` flag is supplied, then the transitive dependencies of the jar will
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
     * Query the central repos directly for the versions info, and
     * optionally, also the transitive dependencies for the artifact.
     *
     * @param result
     * @throws JobException
     */
    private void queryRepos(final DepCommandResult result) throws JobException {
        final String artifactName = groupId + File.separator + artifactId;
        final RepositoryLoader repoLoader = RepositoryLoaderFactory.getLoader();
        String metadataUrl = null;

        try {
            metadataUrl = repoLoader.constructMetadataUrl(groupId, artifactId);
        } catch (RepositoryLoaderException e) {
            throw new JobException(e.getErrorString());
        }

        // get the maven metadata file and populate version info
        String versionStrings = null;
        versionStrings = getVersionsForDependency(artifactName, metadataUrl);
        result.setVersions(versionStrings);

        if (!showDependencies) {
            return;
        }

        // if the --show-dependencies flag was supplied, then
        // get the jar POM (using version) and populate
        // the transitive dependency information.
        String pomUrl = null;
        try {
            pomUrl = repoLoader.constructPOMUrl(groupId, artifactId, version);
        } catch (RepositoryLoaderException e) {
            throw new JobException(e.getErrorString());
        }

        final Artifact suppliedArtifact = new Artifact(groupId, artifactId, version);
        final String depsStrings = getTransitiveDependencies(repoLoader, suppliedArtifact, artifactName, pomUrl, versionStrings);

        if (depsStrings != null) {
            result.setDependenciesInformationAvailable(true);
            result.setDependencyGraphString(depsStrings);
        }
    }

    /**
     * Retrieve the transitive dependencies of the artifact.
     * <p>
     * Create a new Dependency Graph (not the same as the project's
     * Dependency Graph) of this artifact's transitive dependencies.
     * Then invoke DFS on the Dependency Graph with the proper callback
     * that will collect the output into a nicely formatted string.
     * <p>
     * Note that validation of the supplied artifact has already been
     * done by this stage, so no further validation on the supplied
     * artifact is required apart from checking the provdied version
     * string.
     *
     * @param repoLoader
     * @param suppliedArtifact
     * @param artifact
     * @param pomUrl
     * @param validVersions
     * @return
     */
    private String getTransitiveDependencies(final RepositoryLoader repoLoader, final Artifact suppliedArtifact,
                                             final String artifact, final String pomUrl, final String validVersions) throws JobException {
        if (version == null || !validVersions.contains(version)) {
            throw new JobException(String.format("\"%s\" is not a valid version for artifact \"%s\"\n",
                    version, artifact));
        }

        // create the new dependency graph and update it
        final DependencyGraph g = createDependencyGraph();
        final GraphIdGenerator gen = new GraphIdGenerator();
        final int id = gen.getId();

        g.getG().addVertex(id);
        g.getArtifactMapping().put(id, suppliedArtifact);

        // update the dependency graph with this dependency' dependencies
        // (depth-first exploration)
        try {
            updateTransitiveDependencies(g, suppliedArtifact, gen, repoLoader, id);
        } catch (DependencyResolverException e) {
            throw new JobException(e.getErrorString());
        }

        // perform DFS and collect the results into
        // a formatted dependency tree
        final GraphCallback<Integer> depsCallback = new GraphDisplayCallback(g.getArtifactMapping());
        Algorithms.dfs(g.getG(), 0, depsCallback);

        final String formattedDepsTree = ((GraphDisplayCallback) depsCallback).getBuffer().toString();

        return formattedDepsTree;
    }

    /**
     * Create a ne Dependency Graph.
     *
     * @return
     */
    private DependencyGraph createDependencyGraph() {
        final Graph graph = GraphFactory.getGraphImpl(GraphImplType.ADJACENCY_SET, GraphKind.DIRECTED);
        final Map<Integer, Artifact> mapping = new HashMap<>();
        final DependencyGraph g = new DependencyGraph(graph, mapping);

        return g;
    }


    /**
     * Update the dependency graph with the transitive dependencies of the project dependencies.
     * <p>
     * 1. For each project dependency, construct the POM URL for that project, and download the `dependencies`
     * section from that POM file.
     * <p>
     * 2. For each POM dependency, construct its POM URL and query for the POM, and download its own dependencies.
     * <p>
     * 4. This process is carried out for each dependency till such time as no POM is found and/or no further
     * dependencies are found.
     * <p>
     * 5. The updated dependency graph is now ready for further analysis.
     *
     * @param g
     * @param dep
     * @param gen
     * @parama gen
     * @parama repoLoader
     * @parama srcId
     */
    private void updateTransitiveDependencies(final DependencyGraph g, final Artifact dep,
                                              final GraphIdGenerator gen, final RepositoryLoader repoLoader,
                                              int srcId) throws DependencyResolverException {
        String pomUrl = null;

        try {
            pomUrl = repoLoader.constructPOMUrl(dep.getGroupId(), dep.getArtifactId(), dep.getVersion());
        } catch (RepositoryLoaderException e) {
            throw new DependencyResolverException(String.format("resolver failed: %s\n", e.getErrorString()));
        }

        DependencyParser depParser = null;
        try {
            depParser = DependencyParserFactory.getParser(DependencyParserKind.POM, pomUrl);
            depParser.parse();
        } catch (DependencyManagerException e) {
            throw new DependencyResolverException(String.format("resolver failed: %s\n", e.getErrorString()));
        }

        final Dependencies transDepsWrapper = depParser.getDependencies();

        if (transDepsWrapper == null) {
            return;
        }

        final List<Artifact> transDeps = transDepsWrapper.getDependencies();
        if (transDeps == null || transDeps.isEmpty()) {
            return;
        }

        for (final Artifact transDep : transDeps) {
            final int id = gen.getId();
            g.getG().addVertex(id);
            g.getG().addEdge(srcId, id);
            g.getArtifactMapping().put(id, transDep);

            updateTransitiveDependencies(g, transDep, gen, repoLoader, id);
        }
    }

    /**
     * Retrieve the versions information from the maven-metadata.xml file
     * at the proper url.
     *
     * @param artifact
     * @param metadataUrl
     * @return
     */
    private String getVersionsForDependency(final String artifact, final String metadataUrl) throws JobException {
        final DependencyParser parser = DependencyParserFactory.getParser(DependencyParserKind.METADATA, metadataUrl);
        try {
            parser.parse();
        } catch (DependencyManagerException e) {
            throw new JobException(String.format("%s\n\"%s\" does not appear to be a valid artifact.\n",
                    e.getErrorString(), artifact));
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
     * @param result
     * @throws JobException
     */
    private boolean queryLocalCache(final DepCommandResult result) throws JobException {
        return false;
    }

    /**
     * Query the project's Dependency Graph, if available.
     *
     * @param result
     * @throws JobException
     */
    private boolean queryDependencyGraph(final DepCommandResult result) throws JobException {
        return false;
    }
}
