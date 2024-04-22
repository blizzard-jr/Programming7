package org.example;
//Журнал КОД от яндекс, почитать

import org.example.exceptions.*;
import org.example.island.commands.Command;
import org.example.island.commands.Help;
import org.example.island.commands.History;
import org.example.island.commands.Message;
import org.example.island.details.Serialization;
import org.example.island.object.*;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;



/**
 * Класс для взаимодействия с клиентом
 */

public class UserInterface {
    private static Scanner scanner = new Scanner(System.in);
    private static ByteBuffer buffer = ByteBuffer.allocate(4096);
    private static SocketChannel channel;
    public static void main(String[] args){
        try {
            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress("localhost", 4004));
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
            if(ans.isEmpty()){
                Message msg = new Message();
                msg.setArguments(fileName);
                outputData(Serialization.SerializeObject(msg));
                Message answer = inputData();
                System.out.println(answer.getArguments()[0]);
                }
            else{
                System.out.println("Будет использована пустая коллекция");
                Message msg = new Message();
                msg.setArguments("Пустая коллекция");
                outputData(Serialization.SerializeObject(msg));
            }
        }
        else{
            System.out.println("Будет использована пустая коллекция");
            Message msg = new Message();
            msg.setArguments("Пустая коллекция");
            outputData(Serialization.SerializeObject(msg));

        }
        System.out.println("Программа готова к работе");
        while(console.hasNextLine()){
            try {
                Command command = manage.commandForming(console.readWithMessage("---"));
                if(command.getClass() != Help.class & command.getClass() != History.class){
                    outputData(Serialization.SerializeObject(command));
                    Message msg = inputData();
                    for(Object o : msg.getArguments()){
                        System.out.println(o);
                    }
                }
                else{
                    continue;
                }

            }catch(IllegalValueException | NoSuchCommandException | org.example.island.details.exceptions.NoSuchCommandException e){
                console.writeErr(e.getMessage());
            }catch(NoSuchElementException e){
                System.err.println("Программа завершена без сохранения данных");
                System.exit(0);
            }
        }
    }
    public static void outputData(byte[] data){
        try {
            buffer.put(data);
            buffer.flip();
            channel.write(buffer);
            buffer.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Message inputData(){
        try {
            channel.read(buffer);
            buffer.flip();
            return Serialization.DeserializeObject(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            buffer.clear();
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
