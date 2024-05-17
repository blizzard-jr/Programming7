package org.example;
//Журнал КОД от яндекс, почитать

import org.example.exceptions.*;
import org.example.island.commands.Command;
import org.example.island.commands.Help;
import org.example.island.commands.History;
import org.example.island.commands.Message;
import org.example.island.details.Serialization;
import org.example.island.details.Service;
import org.example.island.object.*;



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
    private static InputProcess console = new InputProcess();
    private static CommandsManager manage = new CommandsManager();

    public static void main(String[] args) {
        System.out.println("Начинаем подключение к серверу\n*Пока соединение не будет завершено программа находиться в режиме ожидания\nВыйти можно по команде - \"exit\"");
        connect();
        System.out.println("Соединение с сервером установлено");
        collectionInit(args);
        System.out.println("Программа готова к работе");
        process();
    }

    public static void collectionInit(String[] args) {
        String fileName = "";
        try {
            fileName = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Кажется, вы забыли передать имя файла");
        }
        LinkedHashMap<Integer, StudyGroup> map;
        if (!fileName.isEmpty()) {
            System.out.println("Желаете инициализировать коллекцию из файла? \"Enter\" - Да; Another - Нет: ");
            String ans = scanner.nextLine();
            if (ans.isEmpty()) {
                Message msg = new Message();
                msg.setArguments(fileName);
                outputData(Serialization.SerializeObject(msg));
                Message answer = inputData();
                for (Object o : answer.getArguments()) {
                    System.out.println(o);
                }
            } else {
                System.out.println("Будет использована пустая коллекция");
                Message msg = new Message();
                msg.setArguments("Пустая коллекция");
                outputData(Serialization.SerializeObject(msg));
            }
        } else {
            System.out.println("Будет использована пустая коллекция");
            Message msg = new Message();
            msg.setArguments("Пустая коллекция");
            outputData(Serialization.SerializeObject(msg));

        }
    }

    public static void process() {
        while (console.hasNextLine()) {
            try {
                Command command = manage.commandForming(console.readWithMessage("---"));
                outputData(Serialization.SerializeObject(command));
                Message msg = inputData();
                for (Object o : msg.getArguments()) {
                    System.out.println(o.toString());
                }
            } catch (IllegalValueException | NoSuchCommandException |
                     org.example.island.details.exceptions.NoSuchCommandException e) {
                console.writeErr(e.getMessage());
            } catch (NoSuchElementException e) {
                System.err.println("Программа завершена без сохранения данных");
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
                channel.connect(new InetSocketAddress("localhost", 4004));
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
                System.out.println("Соединение с сервером восстановлено, для того, чтобы получить сведения о последней исполненной команды, нажмите - \"Enter\", иначе - \"Любой другой ввод\"");
                String answer = scanner.nextLine();
                if(answer.isEmpty()){
                    Message msg = new Message();
                    msg.setArguments("Архив");
                    outputData(Serialization.SerializeObject(msg));
                }
            } finally {
                buffer.clear();
            }
        }
    }

}