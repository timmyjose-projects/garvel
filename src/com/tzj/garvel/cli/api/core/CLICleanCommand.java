package com.tzj.garvel.cli.api.core;

import com.tzj.garvel.cli.exception.CLIErrorHandler;
import com.tzj.garvel.common.spi.core.command.CommandException;
import com.tzj.garvel.common.spi.core.command.CommandType;
import com.tzj.garvel.common.spi.core.command.param.CleanCommandParams;
import com.tzj.garvel.common.spi.core.command.result.CleanCommandResult;
import com.tzj.garvel.common.util.UtilServiceImpl;
import com.tzj.garvel.core.CoreServiceImpl;

public class CLICleanCommand extends CLICommand {
    private boolean includeLogs;

    public CLICleanCommand(final CLICommandOption opts, final boolean includeLogs) {
        super(opts);
        this.includeLogs = includeLogs;
    }

    @Override
    public void execute() {
        CleanCommandParams params = new CleanCommandParams(includeLogs);
        try {
            CleanCommandResult result = (CleanCommandResult) CoreServiceImpl.INSTANCE.runCommand(CommandType.CLEAN, params);

            checkSuccess(result);
            UtilServiceImpl.INSTANCE.displayFormattedToConsole(true, "Project cleaned up successfully.");
        } catch (CommandException e) {
            CLIErrorHandler.errorAndExit("Unable to clean the project. Reason = %s\n", e.getLocalizedMessage());
        }
    }

    private void checkSuccess(final CleanCommandResult result) {
        if (result.isTargetDirDeleted()) {
            return;
        }

        CLIErrorHandler.errorAndExit("Unable to clean the project. Reason = target dir does not exist or could not be deleted\n");
    }
}
