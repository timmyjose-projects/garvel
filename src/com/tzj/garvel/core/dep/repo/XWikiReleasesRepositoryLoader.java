package com.tzj.garvel.core.dep.repo;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.dep.api.exception.RepositoryLoaderException;
import com.tzj.garvel.core.dep.api.repo.RepositoryKind;
import com.tzj.garvel.core.dep.api.repo.RepositoryLoader;
import com.tzj.garvel.core.net.api.exception.NetworkServiceException;

public class XWikiReleasesRepositoryLoader extends RepositoryLoader {
    public XWikiReleasesRepositoryLoader() {
        kind = RepositoryKind.XWIKI_RELEASES;
        nextLoader = new ApacheReleasesRepositoryLoader();
    }
}
