package com.tzj.garvel.core.dep.resolver;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.cache.api.DependenciesEntry;
import com.tzj.garvel.core.dep.DependencyGraph;
import com.tzj.garvel.core.dep.api.Artifact;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.exception.DependencyResolverException;
import com.tzj.garvel.core.dep.api.exception.GraphCheckedException;
import com.tzj.garvel.core.dep.api.exception.RepositoryLoaderException;
import com.tzj.garvel.core.dep.api.graph.*;
import com.tzj.garvel.core.dep.api.parser.Dependencies;
import com.tzj.garvel.core.dep.api.parser.DependencyParser;
import com.tzj.garvel.core.dep.api.parser.DependencyParserFactory;
import com.tzj.garvel.core.dep.api.parser.DependencyParserKind;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoader;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoaderFactory;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverOperation;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverStrategy;
import com.tzj.garvel.core.dep.graph.Algorithms;
import com.tzj.garvel.core.dep.graph.GraphCollectArtifactsCallback;
import com.tzj.garvel.core.dep.graph.GraphIdGenerator;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;
import com.tzj.garvel.core.parser.api.visitor.semver.SemverKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The basic resolver for dependencies. This resolver does not handle cyclic
 * dependencies. In addition, for version conflicts, it will return the first
 * suitable entry that it finds, leaving any potential errors to the user to handle.
 */
public class SimpleDependencyResolverStrategy implements DependencyResolverStrategy {
    /**
     * 1. From the list of dependencies, construct the Dependency Graph (DG).
     * 2. Analyse the DG by checking to ensure no cyclic dependencies.
     * 3. Return the list of dependencies in the proper order.
     *
     * @param operation
     * @return
     */
    @Override
    public List<Artifact> resolve(final DependencyResolverOperation operation) throws DependencyResolverException {
        List<Artifact> artifactsOrdering = null;

        switch (operation) {
            case ANALYSE:
                artifactsOrdering = analyse();
                break;
            case CREATE_AND_ANALYSE:
                artifactsOrdering = createAndAnalyse();
                break;
            case UPDATE_AND_ANALYSE:
                artifactsOrdering = updateAndAnalyse();
                break;
        }

        return artifactsOrdering;
    }

    /**
     * 1. Get the list of all dependencies from the cache.
     * 2. Update the existing dependency graph (insertions, deletions, modifications).
     * 3. For each new or changed, dependency in the list above, create a new vertex
     * in the graph and assign a unique id to it.
     * 4. For each dependency, explore and retrieve the transitive
     * dependencies using their POM files, and create new vertices
     * and edges along the way.
     * 5. Finally analyse using Topological Sort to ensure no cycles in
     * the dependencies.
     * 6. Construct the dependencies ordering from the Topological Sort
     * result, and return this as a list.
     * 7. Save the updated dependency graph.
     *
     * @return
     */
    private List<Artifact> updateAndAnalyse() throws DependencyResolverException {
        //@TODO Implement the above.For now, it simply redirects to createAndAnalyse
        return createAndAnalyse();
    }

    /**
     * 1. Get the list of all dependencies from the Core Cache.
     * 2. Create a new directed Graph.
     * 3. For each dependency in the list above, create a new vertex
     * in the graph and assign a unique id to it.
     * 4. For each dependency, explore and retrieve the transitive
     * dependencies using their POM files, and create new vertices
     * and edges along the way.
     * 5. Finally analyse using Topological Sort to ensure no cycles in
     * the dependencies.
     * 6. Construct the dependencies ordering from the Topological Sort
     * result, and return this as a list.
     * 7. Save the new dependency graph.
     *
     * @return
     */
    private List<Artifact> createAndAnalyse() throws DependencyResolverException {
        List<Artifact> artifactsOrdering = new ArrayList<>();

        // create the dependency graph
        final DependencyGraph dependencyGraph = createNewDependencyGraph();

        // get the list of all dependencies from the Core Cache (Config Dependencies)
        final DependenciesEntry projectDependencies = CoreModuleLoader.INSTANCE.getCacheManager().getConfigDependencies();
        List<Artifact> sanitizedProjectDependencies = sanitizeProjectDependencies(projectDependencies);

        // add the project dependencies as vertices of the graph. and
        // update the dependency graph with the transitive dependencies of
        // each project dependency
        final GraphIdGenerator idGenerator = new GraphIdGenerator();
        updateDependencyGraphWithProjectDependencies(dependencyGraph, sanitizedProjectDependencies, idGenerator);

        // analyse using Topological Sort - artifactsOrdering now
        // contains the correct ordering of dependencies.
        doTopologicalAnalysis(dependencyGraph, artifactsOrdering);

        store(dependencyGraph);

        return artifactsOrdering;
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
    private void updateDependencyGraphWithTransitiveDependencies(final DependencyGraph g, final Artifact dep, final GraphIdGenerator gen, final RepositoryLoader repoLoader, int srcId) throws DependencyResolverException {
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
            e.printStackTrace();
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

            updateDependencyGraphWithTransitiveDependencies(g, transDep, gen, repoLoader, id);
        }
    }

    /**
     * Add the project dependencies as vertices of the dependency graph, and update the mapping of their ids to their
     * Artifact objects.
     *
     * @param g
     * @param deps
     * @param gen
     */
    private void updateDependencyGraphWithProjectDependencies(final DependencyGraph g, final List<Artifact> deps, final GraphIdGenerator gen) throws DependencyResolverException {
        final RepositoryLoader repoLoader = RepositoryLoaderFactory.getLoader();

        for (final Artifact dep : deps) {
            final int id = gen.getId();
            g.getG().addVertex(id);
            g.getArtifactMapping().put(id, dep);

            // update the dependency graph with this dependency' dependencies
            // (depth-first exploration)
            updateDependencyGraphWithTransitiveDependencies(g, dep, gen, repoLoader, id);
        }
    }

    private DependencyGraph createNewDependencyGraph() {
        final Graph g = GraphFactory.getGraphImpl(GraphImplType.ADJACENCY_SET, GraphKind.DIRECTED);
        final Map<Integer, Artifact> artifactMapping = new HashMap<>();
        final DependencyGraph dependencyGraph = new DependencyGraph(g, artifactMapping);

        return dependencyGraph;
    }

    private List<Artifact> sanitizeProjectDependencies(final DependenciesEntry projectDependencies) throws DependencyResolverException {
        List<Artifact> artifacts = new ArrayList<>();

        final Map<String, Map<SemverKey, List<String>>> deps = projectDependencies.getDependencies();
        if (deps == null) {
            throw new DependencyResolverException("dependencies cache is empty!\n");
        }

        for (Map.Entry<String, Map<SemverKey, List<String>>> dep : deps.entrySet()) {
            final Artifact artifact = new Artifact();
            final String artifactId = dep.getKey();

            final String[] parts = artifactId.split("/");
            if (parts == null || parts.length != 2 || parts[0] == null || parts[0].isEmpty() || parts[1] == null || parts[1].isEmpty()) {
                throw new DependencyResolverException("resolver failed to get artifact information");
            }

            artifact.setGroupId(parts[0]);
            artifact.setArtifactId(parts[1]);

            final Map<SemverKey, List<String>> versionInfo = dep.getValue();
            if (versionInfo == null || versionInfo.isEmpty()) {
                throw new DependencyResolverException("resolver failed to get artifact information");
            }

            StringBuffer versionBuffer = new StringBuffer();
            if (versionInfo.containsKey(SemverKey.MAJOR)) {
                versionBuffer.append(versionInfo.get(SemverKey.MAJOR).get(0));
            }

            if (versionInfo.containsKey(SemverKey.MINOR)) {
                versionBuffer.append(".");
                versionBuffer.append(versionInfo.get(SemverKey.MINOR).get(0));
            }

            if (versionInfo.containsKey(SemverKey.PATCH)) {
                versionBuffer.append(".");
                versionBuffer.append(versionInfo.get(SemverKey.PATCH).get(0));
            }

            if (versionInfo.containsKey(SemverKey.PRERELEASE)) {
                versionBuffer.append("-");
                versionBuffer.append(versionInfo.get(SemverKey.PRERELEASE).get(0));
            }

            if (versionInfo.containsKey(SemverKey.BUILD)) {
                versionBuffer.append("+");
                versionBuffer.append(versionInfo.get(SemverKey.BUILD).get(0));
            }

            artifact.setVersion(versionBuffer.toString());
            artifacts.add(artifact);
        }

        return artifacts;
    }

    /**
     * Simply use the existing Dependency Graph to get the list of
     * artifacts in the order of dependencies.
     *
     * @return
     */
    private List<Artifact> analyse() throws DependencyResolverException {
        final DependencyGraph dependencyGraph;
        try {
            dependencyGraph = CoreModuleLoader.INSTANCE.getFileSystemFramework().loadSerializedObject(GarvelCoreConstants.GARVEL_PROJECT_DEPS_FILE, DependencyGraph.class);
        } catch (FilesystemFrameworkException e) {
            throw new DependencyResolverException(String.format("resolver cannot analyse dependency graph: graph does not exists (%s)\n", e.getErrorString()));
        }

        List<Artifact> artifactsOrdering = new ArrayList<>();

        doTopologicalAnalysis(dependencyGraph, artifactsOrdering);

        store(dependencyGraph);

        return artifactsOrdering;
    }

    /**
     * Carry out Topological Analysis by checking if the dependencies have a cyclic dependency anywhere. If so, fail the
     * process imeediately. Otherwise, return the proper ordering of dependencies for this project. This will be then used to
     * download the dependencies and populate the Garvel Cache (it is done in a separate step to avoid corrupt state, at the
     * cost of performance).
     *
     * @param dependencyGraph
     * @param artifactsOrdering
     * @throws DependencyResolverException
     */
    private void doTopologicalAnalysis(final DependencyGraph dependencyGraph, final List<Artifact> artifactsOrdering) throws DependencyResolverException {
        GraphCallback<List<Integer>> cb = new GraphCollectArtifactsCallback(dependencyGraph.getArtifactMapping(), artifactsOrdering);

        try {
            Algorithms.topologicalSort(dependencyGraph.getG(), cb);
        } catch (GraphCheckedException e) {
            throw new DependencyResolverException(String.format("resolver failed to analyse dependencies: %s\n", e.getErrorString()));
        }
    }

    /**
     * Persist the Dependency Graph. At this point, the target/deps directory is guaranteed to be present.
     *
     * @param dependencyGraph
     * @throws DependencyResolverException
     */
    private void store(final DependencyGraph dependencyGraph) throws DependencyResolverException {
        try {
            CoreModuleLoader.INSTANCE.getFileSystemFramework().storeSerializedObject(dependencyGraph, GarvelCoreConstants.GARVEL_PROJECT_DEPS_FILE);
        } catch (FilesystemFrameworkException e) {
            throw new DependencyResolverException("dependency analysis failed: unable to store dependency graph\n");
        }
    }
}
