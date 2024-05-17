package org.example.answers;

import org.example.island.commands.Message;
import org.example.island.details.Serialization;
import org.example.requests.RequestsManager;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class AnswerManager {
    private Socket socket;
    private OutputStream ou;
    private Message msg = new Message();

    public Message getMsg() {
        return msg;
    }
    public AnswerManager(Socket socket){
        this.socket = socket;
        try {
            ou = socket.getOutputStream();
        } catch (IOException e) {
            RequestsManager.manager.answerForming(e.getMessage());
        }
    }

    public void answerForming(Object data){
        msg.setArguments(data);
    }
    public void answerForming(Object[] data){
        msg.setArguments(data);
    }
    public void answerForming(ArrayList<Object> data){
        msg.setArguments(data);
    }

    public void flush(Message msg){
        byte[] data = Serialization.SerializeObject(msg);
        try {
            if(data != null){
                ou.write(data);
            }
        } catch (IOException e) {
            RequestsManager.manager.answerForming(e.getMessage());
        }
    }
}
