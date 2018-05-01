package com.tzj.garvel.core.builder.api.jar;

public class JarFileCreatorOptions {
    private String jarFileName;
    private String manifestVersion;
    private String mainClass;
    private String classPathString;

    public JarFileCreatorOptions() {
    }

    public String getClassPathString() {
        return classPathString;
    }

    public void setClassPathString(final String classPathString) {
        this.classPathString = classPathString;
    }

    public String getJarFileName() {
        return jarFileName;
    }

    public void setJarFileName(final String jarFileName) {
        this.jarFileName = jarFileName;
    }

    public String getManifestVersion() {
        return manifestVersion;
    }

    public void setManifestVersion(final String manifestVersion) {
        this.manifestVersion = manifestVersion;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(final String mainClass) {
        this.mainClass = mainClass;
    }
}
