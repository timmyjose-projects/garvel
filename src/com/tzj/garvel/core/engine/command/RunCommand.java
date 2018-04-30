package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandParams;
import com.tzj.garvel.common.spi.core.command.CommandResult;
import com.tzj.garvel.common.spi.core.command.param.BuildCommandParams;
import com.tzj.garvel.common.spi.core.command.param.InstallCommandParams;
import com.tzj.garvel.common.spi.core.command.param.RunCommandParams;
import com.tzj.garvel.common.spi.core.command.result.RunCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.GarvelCoreConstants;
import com.tzj.garvel.core.cache.api.CacheKey;
import com.tzj.garvel.core.cache.api.CacheManagerService;
import com.tzj.garvel.core.cache.api.NameEntry;
import com.tzj.garvel.core.cache.api.VersionEntry;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.Command;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.engine.job.RunJob;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.tzj.garvel.common.parser.GarvelConstants.DASH;
import static com.tzj.garvel.core.dep.api.repo.RepositoryConstants.JAR;

public class RunCommand extends Command {
    public RunCommand() {
        super(new BuildCommand());
    }

    /**
     * `run` depends on `build` , which in turn depends on `install`
     *
     * @throws CommandException
     */
    @Override
    protected void executePrerequisite() throws CommandException {
        // if the target/<project>.jar file exists, then we can skip
        // build
        if (checkProjectArtifactExists()) {
            return;
        }

        try {
            prerequisiteCommand.run(new BuildCommandParams());
        } catch (CommandException e) {
            throw new CommandException(String.format("Prerequisite (build) for run command failed, %s\n", e.getErrorString()));
        }
    }

    private boolean checkProjectArtifactExists() {
        final CacheManagerService cache = CoreModuleLoader.INSTANCE
                .getCacheManager();

        final NameEntry nameEntry = (NameEntry) cache.getEntry(CacheKey.NAME);
        final String projectName = nameEntry.getName();

        final VersionEntry versionEntry = (VersionEntry) cache.getEntry(CacheKey.VERSION);
        final String version = versionEntry.getVersion();

        final String baseJarFileName = projectName + DASH + version + JAR;
        final String qualifiedJarFileName = GarvelCoreConstants.GARVEL_PROJECT_TARGET_DIR +
                File.separator + baseJarFileName;

        return CoreModuleLoader.INSTANCE.getFileSystemFramework().checkFileExists(qualifiedJarFileName);
    }

    @Override
    public CommandResult execute(final CommandParams params) throws CommandException {
        final RunCommandParams cmdParams = (RunCommandParams) params;
        final Job<RunCommandResult> job = new RunJob(cmdParams.getTarget(), cmdParams.getArgs());
        final Future<RunCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().getExecutor().submit(job);

        RunCommandResult cmdRes = null;

        try {
            cmdRes = task.get();
        } catch (InterruptedException e) {
            throw new CommandException("internal exception");
        } catch (ExecutionException e) {
            if (e.getCause() != null) {
                final JobException je = (JobException) e.getCause();
                throw new CommandException(je.getErrorSting());
            }
        }

        return cmdRes;
    }
}
