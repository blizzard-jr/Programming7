package org.example.requests;

import org.example.answers.AnswerManager;
import org.example.island.commands.Command;
import org.example.island.details.Serialization;
import org.example.island.details.exceptions.NoSuchCommandException;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class RequestsManager {
    private Socket socket;
    public static AnswerManager manager;
    public RequestsManager(Socket socket){
        this.socket = socket;
        manager = new AnswerManager(socket);
        processing();
    }
    public void processing(){
        byte[] data = new byte[2048];
        InputStream stream = null;
        try {
            stream = socket.getInputStream();
            stream.read(data);
            Command command = Serialization.DeserializeObject(data);
            command.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
