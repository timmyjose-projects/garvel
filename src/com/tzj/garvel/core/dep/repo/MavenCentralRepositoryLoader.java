package com.tzj.garvel.core.dep.repo;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.dep.api.exception.RepositoryLoaderException;
import com.tzj.garvel.core.dep.api.repo.RepositoryKind;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoader;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

/**
 * This is the main central repository, and will always be tried
 * first to service any requests.
 */
public class MavenCentralRepositoryLoader extends RepositoryLoader {
    public MavenCentralRepositoryLoader() {
        kind = RepositoryKind.CENTRAL;
        nextLoader = new SonaTypeRepositoryLoader();
    }
}
