package org.example.connections;

import org.example.details.Storage;
import org.example.details.StorageOfManagers;
import org.example.fileSystem.FileSystem;
import org.example.requests.RequestsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class connectionManager {
    private static Socket clientSocket;
    private static ServerSocket server;
    static LinkedList<RequestsManager> socketList = new LinkedList<>();
    private final static Logger logger = LoggerFactory.getLogger(connectionManager.class);

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(4004);
        logger.info("Начало работы сервера");
        StorageOfManagers.setStorage(new Storage());
        StorageOfManagers.setFileSystem(new FileSystem());
        String fileName = "";
        try {
            fileName = args[0];
            StorageOfManagers.storage.mapInit(StorageOfManagers.fileSystem.parseToList(fileName));
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("Вы не передали имя файла инициализации и сохранения коллекции\nСделайте это сейчас, иначе клиенты будут работать с пустой коллекцией, а их изменения не будут сохранены\nДля продолжения работы - \"Enter\" или имя файла\n");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            if(answer.isEmpty()){
                StorageOfManagers.storage.mapInit(new LinkedHashMap<>());
                logger.info("Инициализирована пустая коллекция");
            }else{
                fileName = answer;
                StorageOfManagers.storage.mapInit(StorageOfManagers.fileSystem.parseToList(fileName));
                logger.info("Коллекция инициализирована из файла");
            }
        }
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
