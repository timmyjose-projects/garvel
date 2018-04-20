package com.tzj.garvel.core.config.api;

import java.util.List;

public interface ConfigManagerService {
    String getVersion();

    List<String> getInstalledCommands();
}
