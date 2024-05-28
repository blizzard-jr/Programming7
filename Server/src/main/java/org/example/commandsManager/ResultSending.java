package org.example.commandsManager;

import org.example.details.StorageOfManagers;
import org.example.island.commands.Message;

import java.net.Socket;
import java.util.concurrent.RecursiveAction;

public class ResultSending extends RecursiveAction {
    Message msg = new Message();
    Socket clientSocket;
    ResultSending(Message msg, Socket socket){
        this.clientSocket = socket;
        this.msg = msg;
    }
    @Override
    protected void compute() {
        StorageOfManagers.answerManager.flush(msg, clientSocket);
    }
}
