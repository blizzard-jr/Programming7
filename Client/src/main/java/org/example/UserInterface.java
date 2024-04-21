package org.example;
//Журнал КОД от яндекс, почитать

import org.example.exceptions.*;
import org.example.island.commands.Message;
import org.example.island.details.Serialization;
import org.example.island.object.*;


import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;



/**
 * Класс для взаимодействия с клиентом
 */

public class UserInterface {
    private static Scanner scanner = new Scanner(System.in);
    private static Socket clientSocket;
    private BufferedWriter out;
    private BufferedReader in;
    private static ByteBuffer buffer = ByteBuffer.allocate(2048);
    private static SocketChannel channel;
    private final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args){
        try {
            clientSocket = new Socket("localhost", 4004);
            channel = clientSocket.getChannel();
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
        InputProcess console = new InputProcess();
        CommandsManager manage = new CommandsManager();
        LinkedHashMap<Integer, StudyGroup> map;
        if(!fileName.isEmpty()){
            System.out.println("Желаете инициализировать коллекцию из файла? \"Enter\" - Да; Another - Нет: ");
            String ans = scanner.nextLine();
            if(!ans.isEmpty()){
                Message msg = new Message();
                msg.setArguments(fileName);
                outputData(Serialization.SerializeObject(msg));
                String answer = inputData();
                if(answer.equals("Успех")){
                    System.out.println("Коллекция успешно инициализирована");
                }
                else{
                    System.out.println(ans);
                }
            }
            else{
                System.out.println("Будет использована пустая коллекция");
                outputData(Serialization.SerializeObject("Пустая коллекция"));
            }
        }
        else{
            System.out.println("Будет использована пустая коллекция");
            outputData(Serialization.SerializeObject("Пустая коллекция"));

        }
        System.out.println("Программа готова к работе");
        while(console.hasNextLine()){
            try {
                outputData(Serialization.SerializeObject(manage.commandForming(console.readWithMessage("---"))));
            }catch(IllegalValueException e){
                console.writeErr(e.getMessage());
            }catch(NoSuchElementException e){
                System.err.println("Программа завершена без сохранения данных");
                System.exit(0);
            } catch (NoSuchCommandException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void outputData(byte[] data){
        try {
            buffer.put(data);
            channel.write(buffer);
            buffer.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String inputData(){
        try {
            channel.read(buffer);
            buffer.flip();
            return Serialization.DeserializeObject(buffer.array());
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
