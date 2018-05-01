package com.tzj.garvel.core.config;

import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.cache.api.CacheKey;
import com.tzj.garvel.core.cache.api.CacheManagerService;
import com.tzj.garvel.core.cache.api.NameEntry;
import com.tzj.garvel.core.cache.api.VersionEntry;
import com.tzj.garvel.core.config.api.ConfigManagerService;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static com.tzj.garvel.common.parser.GarvelConstants.DASH;
import static com.tzj.garvel.core.dep.api.repo.RepositoryConstants.JAR;

/**
 * Module to handle Garvel and Garvel Project configuration related queries.
 */
public enum ConfigManagerServiceImpl implements ConfigManagerService {
    INSTANCE;

    @Override
    public String getVersion() {
        return GarvelCoreConstants.GARVEL_GARVEL_VERSION;
    }

    @Override
    public List<String> getInstalledCommands() {
        return GarvelCoreConstants.installedCommands;
    }

    /**
     * Check if the project artifact has been generated, and if so, return it.
     *
     * @return
     */
    @Override
    public Path checkProjectJARFileExists() {
        final CacheManagerService cache = CoreModuleLoader.INSTANCE
                .getCacheManager();

        final NameEntry nameEntry = (NameEntry) cache.getEntry(CacheKey.NAME);
        final String projectName = nameEntry.getName();

        final VersionEntry versionEntry = (VersionEntry) cache.getEntry(CacheKey.VERSION);
        final String version = versionEntry.getVersion();

        final String baseJarFileName = projectName + DASH + version + JAR;
        final String qualifiedJarFileName = GarvelCoreConstants.GARVEL_PROJECT_TARGET_DIR +
                File.separator + baseJarFileName;

        return CoreModuleLoader.INSTANCE.getFileSystemFramework().checkFileExistsGetPath(qualifiedJarFileName);
    }
}
