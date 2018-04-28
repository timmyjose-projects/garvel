package com.tzj.garvel.core.dep.api.repo;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.dep.api.exception.RepositoryLoaderException;
import com.tzj.garvel.core.net.api.NetworkConstants;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

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

    public boolean checkRepoStatus() {
        try {
            return CoreModuleLoader.INSTANCE.getNetworkFramework().checkUrlAvailable(kind.getUrl());
        } catch (NetworkServiceException e) {
            return false;
        }
    }

    public String constructArtifactUrl(final String groupId, final String artifactId) throws RepositoryLoaderException {
        if (checkRepoStatus()) {
            // replace the periods with forwards slashes to construct the correct URL
            String modGroupId = groupId.replace(".", "/");
            return kind.getUrl() + NetworkConstants.FORWARD_SLASH + modGroupId + NetworkConstants.FORWARD_SLASH + artifactId;
        }

        return nextLoader.constructArtifactUrl(groupId, artifactId);
    }
}
