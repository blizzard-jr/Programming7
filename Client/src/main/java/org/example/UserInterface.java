package org.example;
//Журнал КОД от яндекс, почитать

import org.example.exceptions.*;
import org.example.island.commands.*;
import org.example.island.details.Serialization;
import org.example.island.details.ServiceConst;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;



/**
 * Класс для взаимодействия с клиентом
 */

public class UserInterface {
    private static Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    private static ByteBuffer buffer = ByteBuffer.allocate(10000);
    private static SocketChannel channel;
    private static Selector selectorConnect;
    protected static InputProcess console = new InputProcess();
    private static CommandsManager manage = new CommandsManager();
    private static int port;

    public static void main(String[] args) {
        System.out.println("Укажите порт сервера для подключения: ");
        port = Integer.parseInt(scanner.nextLine());
        System.out.println("Начинаем подключение к серверу\n*Пока соединение не будет завершено программа находиться в режиме ожидания\nВыйти можно по команде - \"exit\"");
        connect();
        System.out.println("Соединение с сервером установлено");
        System.out.println("Введите логин или нажмите \"Enter\" для регистрации:");
        String answer = scanner.nextLine();
        Object[] userdata;
        if(answer.isEmpty()){
            userdata = registration(new Message());
        }
        else{
            userdata = authentication(new Message(), answer);
        }
        System.out.println("Программа готова к работе");
        userdata = Arrays.copyOfRange(userdata, 1, userdata.length);
        process(userdata);
    }

    public static Object[] authentication(Message data, String login){
        Object[] userdata = new Object[3];
        Message bool;
        int i = 1;
        do {
            data.clearArg();
            if(i > 1) {
                System.out.println("Введите новый логин или \"Enter\" для выхода:");
                String ans = scanner.nextLine();
                if (!ans.isEmpty()) {
                    userdata[1] = ans;
                }else{
                    System.out.println("Всего доброго");
                    System.exit(0);
                }
            }
            else{
                userdata[1] = login;
            }
            userdata[0] = ServiceConst.AUTHORISATION;
            System.out.println("Введите пароль:");
            String password = scanner.nextLine();
            userdata[2] = password;
            data.setArguments(userdata);
            outputData(Serialization.SerializeObject(data));
            bool = inputData();
            for(Object str : bool.getArguments()){
                System.out.println(str);
            }
            i ++;
        }while (!bool.getArguments().get(0).equals("Авторизация прошла успешно"));
        return userdata;
    }
    public static Object[] registration(Message data) {
        Object[] userdata = new Object[3];
        Message bool;
        int i = 0;
        do {
            data.clearArg();
            System.out.println("Придумайте логин:");
            String login = scanner.nextLine();
            System.out.println("Придумайте пароль:");
            String password = scanner.nextLine();
            userdata[0] = ServiceConst.REGISTRATION;
            userdata[1] = login;
            userdata[2] = password;
            data.setArguments(userdata);
            outputData(Serialization.SerializeObject(data));
            bool = inputData();
            for (Object str : bool.getArguments()) {
                System.out.println(str);
            }
            i++;
            if (i > 1) {
                System.out.println("Попробуйте ещё раз - \"Tap something\" или \"Enter\" для выхода:");
                String ans = scanner.nextLine();
                if (!ans.isEmpty()) {
                    continue;
                } else {
                    System.out.println("Всего доброго");
                    System.exit(0);
                }
            }
        }while (!bool.getArguments().get(0).equals("Регистрация прошла успешно"));
            return userdata;
        }
    public static void process(Object[] userData) {
        while (console.hasNextLine()) {
            try {
                Command command = manage.commandForming(console.readWithMessage("---"));
                command.setArguments(userData);
                outputData(Serialization.SerializeObject(command));
                Message msg = inputData();
                if (msg != null) {
                    for (Object o : msg.getArguments()) {
                        System.out.println(o.toString());
                    }
                    if (command.getClass() == (Exit.class)) {
                        System.exit(0);
                    }
                }
                if(command.getClass() == Execute_script.class){
                    boolean flag = false;
                    while(!flag) {
                        msg = inputData();
                        if (msg != null) {
                            for (Object o : msg.getArguments()) {
                                System.out.println(o.toString());
                                if(o.equals("Выполнение скрипта 1 завершено")){
                                    flag = true;
                                }
                            }
                        }
                    }
                }
            } catch (IllegalValueException | NoSuchCommandException |
                     org.example.island.details.exceptions.NoSuchCommandException e) {
                console.writeErr(e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("Работа клиента завершена");
                outputData(Serialization.SerializeObject(new Exit()));
                System.exit(0);
            }
        }
        System.out.println("Работа клиента завершена");
        outputData(Serialization.SerializeObject(new Exit()));
        System.exit(0);
    }

    public static void connect() {
        String ans = "";
        char[] chars = new char[10];
        InputStreamReader reader = new InputStreamReader(System.in);
        while (true) {
            if(ans.equals("exit")){
                System.out.println("Всего доброго!");
                System.exit(0);
            }
            try {
                channel = SocketChannel.open();
                channel.configureBlocking(false);
                channel.connect(new InetSocketAddress("localhost", port));
                selectorConnect = Selector.open();
                SelectionKey selectionKeyConnect = channel.register(selectorConnect, SelectionKey.OP_CONNECT);
                if (selectorConnect.select() > 0) {
                    while (!channel.finishConnect()) {
                        continue;
                    }
                }
                break;
            } catch (IOException e) {
                try {
                    if(reader.ready()){
                        reader.read(chars, 0, 9);
                        ans = new String(Arrays.copyOfRange(chars, 0, 4));
                        chars = new char[10];
                    }
                    else{
                        continue;
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public static void outputData(byte[] data) {
        try {
            buffer.put(data);
            buffer.flip();
            channel.write(buffer);
        } catch (IOException e) {
            System.err.println("Соединение с сервером разорвано, начинаем процесс переподключения\n*Пока соединение не будет завершено программа находиться в режиме ожидания\nВыйти можно по команде - \"exit\"");
            connect();
            System.out.println("Соединение с сервером восстановлено\nПоследняя операция не была обработана, вы можете повторить её");
        } finally {
            buffer.clear();
        }
    }


    public static Message inputData() {
        while (true) {
            try {
                channel.read(buffer);
                if (buffer.position() == 0) {
                    continue;
                } else {
                    buffer.flip();
                    Message message = Serialization.DeserializeObject(buffer.array());
                    return message;
                }
            } catch (IOException e) {
                System.err.println("Соединение с сервером разорвано, начинаем процесс переподключения\n*Пока соединение не будет завершено программа находиться в режиме ожидания\nВыйти можно по команде - \"exit\"");
                connect();
                System.out.println("Соединение с сервером восстановлено");
                return null;
            } finally {
                buffer.clear();
            }
        }
    }

}