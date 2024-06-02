package org.example.requests;

import org.example.answers.AnswerManager;
import org.example.commandsManager.ExecuteManager;
import org.example.commandsManager.Task;
import org.example.details.StorageOfManagers;
import org.example.island.commands.Command;
import org.example.island.commands.Message;
import org.example.island.details.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class RequestsManager implements Runnable{
    private Socket socket;
    private final static Logger logger = LoggerFactory.getLogger(RequestsManager.class);
    public static boolean clientContinue;
    private ForkJoinPool processingPool = ForkJoinPool.commonPool();

    public RequestsManager(Socket socket){
        clientContinue = true;
        this.socket = socket;
    }

    @Override
    public void run() {
        byte[] data = new byte[10000];
        InputStream stream = null;
        Message message = new Message();
        try {
            stream = socket.getInputStream();
            while (true){
                int t = stream.read(data);
                Command command = Serialization.DeserializeObject(data);
                logger.info("Обработка запроса");
                command.setArguments(socket);
                processingPool.invoke(new Task(command));
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
    public Message getMessage() {
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
