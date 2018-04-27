package com.tzj.garvel.core.dep.api.repo;

import com.tzj.garvel.core.dep.api.exception.RepositoryLoaderException;

/**
 * The common functionality associated with a repository goes
 * here.
 */
public abstract class RepositoryLoader {
    protected RepositoryLoader nextLoader;
    protected RepositoryKind kind;

    public RepositoryLoader() {
    }

    public RepositoryKind getKind() {
        return kind;
    }

    protected RepositoryLoader getNextLoader() {
        return nextLoader;
    }

    protected abstract boolean checkRepoStatus();

    public abstract String constructArtifactUrl(final String groupId, final String artifactId) throws RepositoryLoaderException;
}
