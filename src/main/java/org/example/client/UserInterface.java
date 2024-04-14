package org.example.client;
//Журнал КОД от яндекс, почитать

import org.example.object.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.server.details.StorageOfManagers.fileSystem;


/**
 * Класс для взаимодействия с клиентом
 */

public class UserInterface {
    private static Socket clientSocket;
    private final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args){
        try {
            clientSocket = new Socket("localhost", 4004);
            System.out.println("Соединение с сервером установлено");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fileName = "";
        try{
            fileName = args[0];
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Кажется, вы забыли передать имя файла");
        }

        System.out.println(fileName);
        Storage st = new Storage();
        FileSystem file = new FileSystem();
        UserInterface user = new UserInterface();
        CommandsManager manage = new CommandsManager();
        CollectionManager collection = new CollectionManager();
        StorageOfManagers storageOfManagers = new StorageOfManagers(collection, user, file, manage, st);
        LinkedHashMap<Integer, StudyGroup> map;
        if(!fileName.isEmpty()){
            map = user.readFile("Желаете инициализировать коллекцию из файла? \"Enter\" - Да; Another - Нет: ", fileName);
        }
        else{
            System.out.println("Будет использована пустая коллекция");
            map = new LinkedHashMap<>();
        }
        StorageOfManagers.storage.mapInit(map);
        user.writeln("Программа готова к работе");
        while(user.hasNextLine()){
            try {
                manage.executeCommand(user.readWithMessage(""));
            }catch(NoSuchCommandException | IllegalValueException e){
                user.writeErr(e.getMessage());
            }catch(NoSuchElementException e){
                System.err.println("Программа завершена без сохранения данных");
                System.exit(0);
            }
        }
    }





















































    /*
    public void writeln(Object o){
        System.out.println(o);
    }

    /**
     * Метод для инициализации коллекции значениями из файла
     * @param s     Имя файла
     * @return map
     */
/*
    public LinkedHashMap<Integer, StudyGroup> readFile(String s, String fileName) {
        writeln(s);
        String answer = scanner.nextLine();
        while(true){
            if(answer.isEmpty()){
                try{
                    return fileSystem.parseToList(fileName);
                }
                catch(IOException e){
                    if(e.getClass() == com.fasterxml.jackson.databind.exc.InvalidDefinitionException.class){
                        System.out.println("Значения в файле не валидны или нарушен формат json, введите новое имя, \\\"Enter\\\" - для использования пустой коллекции или 1 для выхода: \"");
                    }
                    else{
                        System.out.println("Не удалось получить данные из указанного файла, введите новое имя, \"Enter\" - для использования пустой коллекции или 1 для выхода: ");
                    }
                    String ans = scanner.nextLine();
                    if(ans.equals("1")){
                        System.out.println("Всего доброго");
                        System.exit(0);
                        }
                    else if(ans.isEmpty()){
                        System.out.println("Будет использована пустая коллекция");
                        return new LinkedHashMap<>();
                        }
                    else {
                        fileName = ans;
                        }
                    }
                }
            else {
                fileSystem.setFileName(fileName);
                return new LinkedHashMap<>();
                }
            }
        }



 */



}
