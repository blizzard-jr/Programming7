package org.example.commandsManager;

import org.example.details.StorageOfManagers;
import org.island.commands.Command;

import java.util.concurrent.RecursiveAction;

public class Task extends RecursiveAction {
    private final Command command;
    public Task(Command cmd){
        this.command = cmd;
    }
    @Override
    protected void compute() {
        StorageOfManagers.executeManager.commandExecute(command);
    }
}
