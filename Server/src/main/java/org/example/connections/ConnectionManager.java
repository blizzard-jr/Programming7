package org.example.connections;

import org.example.requests.RequestsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionManager {
    private static Socket clientSocket;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final static Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private ServerSocket server;
    public volatile static int clientCount = 0;
    public ConnectionManager(ServerSocket server){
        this.server = server;
    }
    public void waiting(){
        try{
            while(true){
                clientSocket = server.accept();
                logger.info("Получено новое подключение, " + (clientCount + 1) + " активных клиентов");
                clientCount++;
                executor.execute(new RequestsManager(clientSocket));
            }
        } catch (Error | IOException e) {
            logger.error("На сервере произошла непредвиденная ошибка, работа сервера завершена: " + e.getCause());
            System.exit(0);
        }
    }
}
