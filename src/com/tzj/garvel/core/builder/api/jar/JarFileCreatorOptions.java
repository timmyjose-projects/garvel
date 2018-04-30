package com.tzj.garvel.core.builder.api.jar;

public class JarFileCreatorOptions {
    private String jarFileName;
    private String manifestVersion;
    private String mainClass;

    public JarFileCreatorOptions(final String jarFileName, final String manifestVersion, final String mainClass) {
        this.jarFileName = jarFileName;
        this.manifestVersion = manifestVersion;
        this.mainClass = mainClass;
    }

    public JarFileCreatorOptions(final String jarFileName, final String manifestVersion) {
        this.jarFileName = jarFileName;
        this.manifestVersion = manifestVersion;
    }

    public String getJarFileName() {
        return jarFileName;
    }

    public String getManifestVersion() {
        return manifestVersion;
    }

    public String getMainClass() {
        return mainClass;
    }
}
