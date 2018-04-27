package com.tzj.garvel.core.dep.repo;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.dep.api.exception.RepositoryLoaderException;
import com.tzj.garvel.core.dep.api.repo.RepositoryKind;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoader;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

public class ApacheReleasesRepositoryLoader extends RepositoryLoader {
    public ApacheReleasesRepositoryLoader() {
        kind = RepositoryKind.APACHE_RELEASES;
        nextLoader = new ClojarsRepositoryLoader();
    }

    @Override
    protected boolean checkRepoStatus() {
        try {
            return CoreModuleLoader.INSTANCE.getNetworkFramework().checkUrlAvailable(kind.getUrl());
        } catch (NetworkServiceException e) {
            return false;
        }
    }

    @Override
    public String constructArtifactUrl(final String groupId, final String artifactId) throws RepositoryLoaderException {
        if (checkRepoStatus()) {
            return kind.getUrl() + GarvelCoreConstants.FORWARD_SLASH + groupId + GarvelCoreConstants.FORWARD_SLASH + artifactId;
        }

        return nextLoader.constructArtifactUrl(groupId, artifactId);
    }
}
