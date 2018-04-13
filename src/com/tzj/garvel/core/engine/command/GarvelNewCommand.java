package com.tzj.garvel.core.engine.command;

import com.tzj.garvel.common.spi.core.command.GarvelCommandParams;
import com.tzj.garvel.common.spi.core.command.GarvelCommandResult;
import com.tzj.garvel.core.CoreModuleLoader;
import com.tzj.garvel.core.concurrent.spi.GarvelJob;
import com.tzj.garvel.core.engine.GarvelCommand;
import com.tzj.garvel.core.engine.task.GarvelNewJob;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GarvelNewCommand implements GarvelCommand {
    @Override
    public GarvelCommandResult execute(final GarvelCommandParams params) {
        GarvelJob<GarvelCommandResult> job = new GarvelNewJob<>();
        Future<GarvelCommandResult> task = CoreModuleLoader.INSTANCE.getConcurrencyFramework().submitTask(job);

        GarvelCommandResult cmdRes = null;
        try {
            // add a timeout value?
            cmdRes = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return cmdRes;
    }
}