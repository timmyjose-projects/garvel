package com.tzj.garvel.core.builder.api;

public class JarFileFields {
    private String jarFileName;
    private int manifestVersion;
    private String mainClass;

    public JarFileFields(final String jarFileName, final int manifestVersion, final String mainClass) {

        this.jarFileName = jarFileName;
        this.manifestVersion = manifestVersion;
        this.mainClass = mainClass;
    }

    public String getJarFileName() {
        return jarFileName;
    }

    public int getManifestVersion() {
        return manifestVersion;
    }

    public String getMainClass() {
        return mainClass;
    }
}
