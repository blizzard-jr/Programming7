package org.example.client;
//Журнал КОД от яндекс, почитать

import com.sun.source.doctree.SeeTree;
import org.example.commands.Command;
import org.example.object.*;
import org.example.server.details.Storage;
import org.example.server.details.StorageOfManagers;
import org.example.server.exceptions.IllegalValueException;
import org.example.server.exceptions.NoSuchCommandException;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;



/**
 * Класс для взаимодействия с клиентом
 */

public class UserInterface {
    private static Socket clientSocket;
    private BufferedWriter out;
    private BufferedReader in;
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
        InputProcess console = new InputProcess();
        CommandsManager manage = new CommandsManager();
        //CollectionManager collection = new CollectionManager();
        StorageOfManagers storageOfManagers = new StorageOfManagers(collection, console, file, manage, st);
        LinkedHashMap<Integer, StudyGroup> map;
        if(!fileName.isEmpty()){
            map = console.readFile("Желаете инициализировать коллекцию из файла? \"Enter\" - Да; Another - Нет: ", fileName);
        }
        else{
            System.out.println("Будет использована пустая коллекция");
            map = new LinkedHashMap<>();
        }
        StorageOfManagers.storage.mapInit(map);
        console.writeln("Программа готова к работе");
        while(console.hasNextLine()){
            try {
                outputData(manage.commandForming(console.readWithMessage("")));
            }catch(NoSuchCommandException | IllegalValueException e){
                console.writeErr(e.getMessage());
            }catch(NoSuchElementException e){
                System.err.println("Программа завершена без сохранения данных");
                System.exit(0);
            }
        }
    }
    public static void outputData(ByteBuffer buffer){
        try {
            OutputStream ou = clientSocket.getOutputStream();
            ou.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
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
