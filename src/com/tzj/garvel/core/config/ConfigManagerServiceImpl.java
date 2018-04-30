package com.tzj.garvel.core.config;

import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.config.api.ConfigManagerService;

import java.util.List;

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
}
