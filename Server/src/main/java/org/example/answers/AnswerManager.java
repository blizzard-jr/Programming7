package org.example.answers;

import org.example.island.commands.Message;
import org.example.island.details.Serialization;
import org.example.requests.RequestsManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class AnswerManager {
    private Socket socket;
    private OutputStream ou;

    public AnswerManager(Socket socket){
        this.socket = socket;
        try {
            ou = socket.getOutputStream();
        } catch (IOException e) {
            RequestsManager.manager.answerForming(e.getMessage());
        }
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
        byte[] finalData = new byte[data.length + 1];
        System.arraycopy(data, 0, finalData, 0, data.length);
        finalData[data.length] = (byte) 254;
        try {
            if(finalData != null){
                ou.write(finalData);
            }
        } catch (IOException e) {
            RequestsManager.manager.answerForming(e.getMessage());
        }
    }
}
