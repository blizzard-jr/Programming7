package org.example.commandsManager;

import org.example.details.StorageOfManagers;
import org.example.island.commands.Command;

import java.net.Socket;
import java.util.concurrent.RecursiveAction;

public class Task extends RecursiveAction {
    private final Command command;
    private final Socket clientSocket;
    public Task(Command cmd, Socket socket){
        this.command = cmd;
        this.clientSocket = socket;
    }
    @Override
    protected void compute() {
        StorageOfManagers.executeManager.commandExecute(command, clientSocket);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
