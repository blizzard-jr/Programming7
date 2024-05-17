package org.example.requests;

import org.example.answers.AnswerManager;
import org.example.commandsManager.ExecuteManager;
import org.example.details.StorageOfManagers;
import org.example.island.commands.Command;
import org.example.island.commands.Message;
import org.example.island.details.Serialization;
import org.example.island.details.Service;
import org.example.island.details.exceptions.NoSuchCommandException;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

public class RequestsManager {
    private static Socket socket;
    public static AnswerManager manager;
    public RequestsManager(Socket socket){
        RequestsManager.socket = socket;
        manager = new AnswerManager(socket);
        processing();
    }
    public void processing(){
        byte[] data = new byte[2048];
        InputStream stream = null;
        try {
            stream = socket.getInputStream();
            while (true){
                int t = stream.read(data);
                Command command = Serialization.DeserializeObject(data);
                command.execute(StorageOfManagers.executeManager);
                manager.flush(manager.getMsg());
                manager.getMsg().setArguments(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Message getMessage() {
        byte[] data = new byte[2048];
        InputStream stream = null;
        try {
            stream = socket.getInputStream();
            int t = stream.read(data);
            Message msg = Serialization.DeserializeObject(data);
            return msg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
