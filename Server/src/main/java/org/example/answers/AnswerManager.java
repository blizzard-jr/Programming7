package org.example.answers;

import org.example.island.commands.Message;
import org.example.island.details.Serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class AnswerManager {
    private Socket socket;
    private OutputStream ou;

    public AnswerManager(Socket socket){
        this.socket = socket;
    }

    public void answerForming(Object data){
        Message message = new Message();
        message.setArguments(data);
        flush(message);
    }
    public void answerForming(Object[] data){
        Message msg = new Message();
        msg.setArguments(data);
        flush(msg);
    }
    public void flush(Message msg){
        byte[] data = Serialization.SerializeObject(msg);
        try {
            ou = socket.getOutputStream();
            ou.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
