package com.tzj.garvel.core.dep.resolver;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.dep.DependencyGraph;
import com.tzj.garvel.core.dep.api.Artifact;
import com.tzj.garvel.core.dep.api.exception.DependencyManagerException;
import com.tzj.garvel.core.dep.api.exception.DependencyResolverException;
import com.tzj.garvel.core.dep.api.exception.GraphCheckedException;
import com.tzj.garvel.core.dep.api.graph.GraphCallback;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverOperation;
import com.tzj.garvel.core.dep.api.resolver.DependencyResolverStrategy;
import com.tzj.garvel.core.dep.graph.Algorithms;
import com.tzj.garvel.core.dep.graph.GraphCollectArtifactsCallback;
import com.tzj.garvel.core.filesystem.exception.FilesystemFrameworkException;

import java.util.ArrayList;
import java.util.List;

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
        final DependencyGraph dependencyGraph = null;
        store(dependencyGraph);

        return null;
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
        final DependencyGraph dependencyGraph = null;
        store(dependencyGraph);

        return null;
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
            dependencyGraph = (DependencyGraph) CoreModuleLoader.INSTANCE.getFileSystemFramework().loadSerializedObject(GarvelCoreConstants.GARVEL_PROJECT_DEPS_FILE, DependencyGraph.class);
        } catch (FilesystemFrameworkException e) {
            throw new DependencyResolverException(String.format("resolver cannot analyse dependency graph: graph does not exists (%s)\n", e.getErrorString()));
        }

        List<Artifact> artifactsOrdering = new ArrayList<>();
        GraphCallback<List<Integer>> cb = new GraphCollectArtifactsCallback(dependencyGraph.getArtifactMapping(), artifactsOrdering);

        try {
            Algorithms.topologicalSort(dependencyGraph.getG(), cb);
        } catch (GraphCheckedException e) {
            throw new DependencyResolverException(String.format("dependency analysis failed: %s\n", e.getErrorString()));
        }

        store(dependencyGraph);

        return artifactsOrdering;
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
