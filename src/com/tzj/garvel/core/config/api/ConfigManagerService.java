package com.tzj.garvel.core.config.api;

import java.nio.file.Path;
import java.util.List;

public interface ConfigManagerService {
    String getVersion();

    List<String> getInstalledCommands();

    Path checkProjectJARFileExists();
}
