package org.example;

import org.example.answers.AnswerManager;
import org.example.commandsManager.ExecuteManager;
import org.example.connections.ConnectionManager;
import org.example.details.Storage;
import org.example.details.StorageOfManagers;
import org.example.fileSystem.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private final static Logger logger = LoggerFactory.getLogger(Main.class);
    private static ServerSocket server;
    public static void main(String[] args) throws IOException {
        StorageOfManagers.setStorage(new Storage());
        StorageOfManagers.setFileSystem(new FileSystem());
        StorageOfManagers.setExecuteManager(new ExecuteManager());
        StorageOfManagers.setAnswerManager(new AnswerManager());
        int port = portInit(args);
        server = new ServerSocket(port);
        logger.info("Начало работы сервера");
        collectionInit(args);
        new ConnectionManager(server).waiting();
    }
    public static int portInit(String[] args){
        int port;
        try{
            port = Integer.parseInt(args[0]);
        }catch (NumberFormatException | ArrayIndexOutOfBoundsException e){
            logger.error("Вы не передали номер порта или передали не int значение");
            logger.info("Укажите порт для сервера");
            port = scanner.nextInt();
        }
        return port;
    }
    public static void collectionInit(String[] args){
        String fileName = "";
        try {
            fileName = args[1];
            StorageOfManagers.storage.mapInit(StorageOfManagers.fileSystem.parseToList(fileName));
        } catch (ArrayIndexOutOfBoundsException | IOException e) {
            logger.error("Вы не передали имя файла инициализации и сохранения коллекции\nСделайте это сейчас, иначе клиенты будут работать с пустой коллекцией, а их изменения не будут сохранены\nДля продолжения работы - \"continue\" или имя файла");
            String answer = scanner.next();
            if (answer.equals("continue")) {
                StorageOfManagers.storage.mapInit(new LinkedHashMap<>());
                logger.info("Инициализирована пустая коллекция");
            } else {
                fileName = answer;
                try {
                    StorageOfManagers.storage.mapInit(StorageOfManagers.fileSystem.parseToList(fileName));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                logger.info("Коллекция инициализирована из файла");
            }
        }
    }
}
