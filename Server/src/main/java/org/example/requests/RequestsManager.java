package org.example.requests;

import org.example.answers.AnswerManager;
import org.example.commandsManager.ExecuteManager;
import org.example.details.StorageOfManagers;
import org.example.island.commands.Command;
import org.example.island.commands.Message;
import org.example.island.details.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

public class RequestsManager {
    private static Socket socket;
    public static AnswerManager answerManager;
    private final static Logger logger = LoggerFactory.getLogger(RequestsManager.class);
    public static boolean clientContinue;



    public RequestsManager(Socket socket){
        clientContinue = true;
        RequestsManager.socket = socket;
        answerManager = new AnswerManager(socket);
        StorageOfManagers.setExecuteManager(new ExecuteManager());
        processing();
    }
    public void processing(){
        byte[] data = new byte[2048];
        InputStream stream = null;
        try {
            stream = socket.getInputStream();
            if(StorageOfManagers.fileSystem.getFileName().isEmpty()){
                answerManager.answerForming("В данный момент сервер работает в режиме песочницы, так как коллекция не связана с файлом\nИзменения не будут сохранены после завершения работы");
                answerManager.flush(answerManager.getMsg());
                answerManager.getMsg().setArguments(new ArrayList<>());
            }
            else{
                answerManager.answerForming("Коллекция инициализирована");
                answerManager.flush(answerManager.getMsg());
                answerManager.getMsg().setArguments(new ArrayList<>());
            }
            while (true){
                int t = stream.read(data);
                Command command = Serialization.DeserializeObject(data);
                logger.info("Обработка запроса");
                StorageOfManagers.executeManager.commandExecute(command);//Сделать проверку на ноль
                answerManager.flush(answerManager.getMsg());
                answerManager.getMsg().setArguments(new ArrayList<>());
                if(!clientContinue){
                    break;
                }
            }
        } catch (IOException e) {
            answerManager.answerForming("На этапе обработки выполнения запроса произошёл сбой, вам предлагается переподключиться к серверу");
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
