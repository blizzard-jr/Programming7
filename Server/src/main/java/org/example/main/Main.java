package org.example.main;

import org.example.commandsManager.DataBaseManager;
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
import java.sql.*;
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
        try {
            dbconnection(args[1], args[2], args[3]); // host, login, pass for db connection
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("не удалось установить соединение с БД. программа будет завершена(");
            System.exit(1);
        }
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


    public static void dbconnection(String url, String login, String password) throws ClassNotFoundException, SQLException{
        Scanner scanner = new Scanner(System.in);
        try {
            //Properties info = new Properties();
            Class.forName("org.postgresql.Driver");

            //info.load(new FileInputStream("db.cfg")); //0UW5OUaZxbLaO1Cv

            Connection connection = DriverManager.getConnection(url, login, password);
            StorageOfManagers.setDataBaseManager(new DataBaseManager(connection));
            StorageOfManagers.setFileSystem(new FileSystem());

        } catch (SQLException e) {
            logger.error("Не удалось подключиться к бд по введённым вами данным, повторите попытку");
        }

    }
}
