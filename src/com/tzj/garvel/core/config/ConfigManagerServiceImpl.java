package com.tzj.garvel.core.config;

import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.config.api.ConfigManagerService;

public enum ConfigManagerServiceImpl implements ConfigManagerService {
    INSTANCE;


    @Override
    public String getVersion() {
        return GarvelCoreConstants.GARVEL_VERSION;
    }
}
