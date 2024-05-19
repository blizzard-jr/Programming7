package org.example.connections;


import org.example.commandsManager.ExecuteManager;
import org.example.details.Storage;
import org.example.details.StorageOfManagers;
import org.example.fileSystem.FileSystem;

import org.example.requests.RequestsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class connectionManager {
    private static Socket clientSocket;
    private static ServerSocket server;
    static LinkedList<RequestsManager> socketList = new LinkedList<>();
    private final static Logger logger = LoggerFactory.getLogger(connectionManager.class);

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(4004);
        logger.info("Начало работы сервера");
        StorageOfManagers managers = new StorageOfManagers(new Storage(), new FileSystem(), new ExecuteManager());
        try{
            while(true){
                clientSocket = server.accept();
                logger.info("Получено новое подключение");
                socketList.add(new RequestsManager(clientSocket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally{
            server.close();
        }
    }
}
