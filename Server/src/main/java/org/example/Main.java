package org.example;

import org.example.answers.AnswerManager;
import org.example.commandsManager.ExecuteManager;
import org.example.connections.ConnectionManager;
import org.example.details.Storage;
import org.example.details.StorageOfManagers;
import org.example.fileSystem.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private final static Logger logger = LoggerFactory.getLogger(Main.class);
    private static ServerSocket server;
    public static void main(String[] args) throws IOException {
        StorageOfManagers.setStorage(new Storage());
        StorageOfManagers.setExecuteManager(new ExecuteManager());
        StorageOfManagers.setAnswerManager(new AnswerManager());
        int port = portInit(args);
        server = new ServerSocket(port);
        dbconnection();
        logger.info("Начало работы сервера");
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
    public static void dbconnection(){
        try {
            Properties info = new Properties();
            Class.forName("org.postgresql.Driver");
            info.load(new FileInputStream("db.cfg"));
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:9911/studs", "s409127", "0UW5OUaZxbLaO1Cv");
            StorageOfManagers.setDataBaseManager(new DataBaseManager(connection));
            StorageOfManagers.dBManager.collectionInit();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
