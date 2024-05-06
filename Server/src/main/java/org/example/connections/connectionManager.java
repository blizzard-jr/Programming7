package org.example.connections;


import org.example.answers.AnswerManager;
import org.example.commandsManager.ExecuteManager;
import org.example.details.Storage;
import org.example.details.StorageOfManagers;
import org.example.fileSystem.FileSystem;
import org.example.island.details.Serialization;
import org.example.requests.RequestsManager;

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

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(4004);
        System.out.println("Server is ready!");//Нельзя так, это нужно передать клиенту
        StorageOfManagers managers = new StorageOfManagers(new Storage(), new FileSystem(), new ExecuteManager());
        try{
            while(true){
                clientSocket = server.accept();
                socketList.add(new RequestsManager(clientSocket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally{
            server.close();
        }
    }
}
