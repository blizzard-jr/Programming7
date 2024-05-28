package org.example;
//Журнал КОД от яндекс, почитать

import org.example.exceptions.*;
import org.example.island.commands.*;
import org.example.island.details.Serialization;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;



/**
 * Класс для взаимодействия с клиентом
 */

public class UserInterface {
    private static Scanner scanner = new Scanner(System.in);
    private static ByteBuffer buffer = ByteBuffer.allocate(10000);
    private static SocketChannel channel;
    private static Selector selectorConnect;
    protected static InputProcess console = new InputProcess();
    private static CommandsManager manage = new CommandsManager();
    private static int port;

    public static void main(String[] args) {
        System.out.println("Укажите порт сервера для подключения: ");
        port = scanner.nextInt();
        System.out.println("Начинаем подключение к серверу\n*Пока соединение не будет завершено программа находиться в режиме ожидания\nВыйти можно по команде - \"exit\"");
        connect();
        System.out.println("Соединение с сервером установлено");
        Message msg = inputData();
        if(msg != null) {
            for (Object o : msg.getArguments()) {
                System.out.println(o.toString());
            }
        }
        System.out.println("Программа готова к работе");
        process();
    }


    public static void process() {
        while (console.hasNextLine()) {
            try {
                Command command = manage.commandForming(console.readWithMessage("---"));
                outputData(Serialization.SerializeObject(command));
                Message msg = inputData();
                if(msg != null){
                    for (Object o : msg.getArguments()) {
                        System.out.println(o.toString());
                    }
                    if(command.getClass() == (Exit.class)){
                        System.exit(0);
                    }
                }else{
                    continue;
                }

            } catch (IllegalValueException | NoSuchCommandException |
                     org.example.island.details.exceptions.NoSuchCommandException e) {
                console.writeErr(e.getMessage());
            } catch (NoSuchElementException e) {
                Message m = new Message();
                m.setArguments("Завершение");
                outputData(Serialization.SerializeObject(m));
                Message msg = inputData();
                for (Object o : msg.getArguments()) {
                    System.out.println(o.toString());
                }
                System.exit(0);
            }
        }
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