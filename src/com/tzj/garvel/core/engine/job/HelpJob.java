package com.tzj.garvel.core.engine.job;

import com.tzj.garvel.common.spi.core.command.result.HelpCommandResult;
import com.tzj.garvel.core.concurrent.api.Job;
import com.tzj.garvel.core.engine.exception.JobException;
import com.tzj.garvel.core.engine.job.help.HelpPages;

public class HelpJob implements Job<HelpCommandResult> {
    private final String commandName;

    public HelpJob(final String commandName) {
        this.commandName = commandName;
    }

    @Override
    public HelpCommandResult call() throws JobException {
        String helpPages = null;

        switch (commandName) {
            case "help":
                helpPages = HelpPages.helpCommand;
                break;
            case "version":
                helpPages = HelpPages.versionComand;
                break;
            case "list":
                helpPages = HelpPages.listCommand;
                break;
            case "install":
                helpPages = HelpPages.installCommand;
                break;
            case "uninstall":
                helpPages = HelpPages.uninstallCommand;
                break;
            case "new":
                helpPages = HelpPages.newCommand;
                break;
            case "build":
                helpPages = HelpPages.buildCommand;
                break;
            case "clean":
                helpPages = HelpPages.cleanCommand;
                break;
            case "run":
                helpPages = HelpPages.runCommand;
                break;
            case "dep":
                helpPages = HelpPages.depCommand;
                break;
            default:
                throw new JobException(String.format("Unable to retrieve help pages for unknown command %s", commandName));
        }

        final HelpCommandResult result = new HelpCommandResult(helpPages);

        return result;
    }
}
