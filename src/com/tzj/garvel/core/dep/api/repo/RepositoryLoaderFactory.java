package com.tzj.garvel.core.dep.api.repo;

import com.tzj.garvel.core.dep.repo.MavenCentralRepositoryLoader;

/**
 * The RepositoryLoader hierarchy follows a Chain of
 * Responsibility. The default loader returned is that
 * for Maven Central.
 * In case Garvel cannot connect to that repository, the
 * next loader (as specified in the classes themselves) will
 * be tried until failure.
 * Of course, some of the repositories may not contain the
 * required artifacts, but this works as a simple failsafe
 * mechanism.
 */
public class RepositoryLoaderFactory {
    private RepositoryLoaderFactory() {
    }

    /**
     * The default repository is Maven Central.
     *
     * @return
     */
    public static RepositoryLoader getLoader() {
        return new MavenCentralRepositoryLoader();
    }
}
