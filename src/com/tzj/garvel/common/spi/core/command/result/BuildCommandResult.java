package com.tzj.garvel.common.spi.core.command.result;

import com.tzj.garvel.common.spi.core.command.CommandResult;

import java.nio.file.Path;

public class BuildCommandResult extends CommandResult {
    private Path targetDir;
    private Path buildDir;
    private Path depsDir;
    private Path jarFile;

    public BuildCommandResult() {
    }

    public Path getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(final Path targetDir) {
        this.targetDir = targetDir;
    }

    public Path getBuildDir() {
        return buildDir;
    }

    public void setBuildDir(final Path buildDir) {
        this.buildDir = buildDir;
    }

    public Path getDepsDir() {
        return depsDir;
    }

    public void setDepsDir(final Path depsDir) {
        this.depsDir = depsDir;
    }

    public Path getJarFile() {
        return jarFile;
    }

    public void setJarFile(final Path jarFile) {
        this.jarFile = jarFile;
    }
}
