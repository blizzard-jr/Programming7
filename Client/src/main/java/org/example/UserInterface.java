package org.example;
//Журнал КОД от яндекс, почитать

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controllers.EnterScene;
import org.example.exceptions.*;
import org.island.commands.Command;
import org.island.commands.Execute_script;
import org.island.commands.Exit;
import org.island.commands.Message;
import org.island.details.Serialization;
import org.island.details.ServiceConst;
import org.island.object.TableGroup;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

/**
 * Класс для взаимодействия с клиентом
 */

public class UserInterface extends javafx.application.Application{
    private static String login;
    private static String password;
    private static int port;
    private static boolean register;
    private static ByteBuffer buffer = ByteBuffer.allocate(10000);
    private static SocketChannel channel;
    private static Selector selectorConnect;
    protected static InputProcess console = new InputProcess();
    private static CommandsManager manage = new CommandsManager();
    private static EnterScene enterScene;
    private static Stage stage;
    private static List<TableGroup> data;
    private static Object[] log;

    public static void setData(List<TableGroup> data) {
        UserInterface.data = data;
    }
    public static void main(String[] args){
        Application.launch();
//        connect();
//        Object[] userdata;
//        userdata = authentication(new Message());
//        System.out.println("Программа готова к работе");
//        userdata = Arrays.copyOfRange(userdata, 1, userdata.length);
//        process(userdata);
    }
    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        UserInterface.stage = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getResource("/Entr.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
        enterScene = loader.getController();
    }

    public static Object[] getLog() {
        return log;
    }

    public static String getPassword() {
        return password;
    }

    public static Object[] authentication(Message data){
        Object[] userdata = new Object[3];
        Message bool;
        data.clearArg();
        if(register){
            userdata[0] = ServiceConst.REGISTRATION;
        }
        else{
            userdata[0] = ServiceConst.AUTHORISATION;
        }
        userdata[2] = password;
        userdata[1] = login;
        log = Arrays.copyOfRange(userdata, 1, userdata.length);
        data.setArguments(userdata);
        outputData(Serialization.SerializeObject(data));
        bool = inputData();
        enterScene.exceptionMessage(null, (String) bool.getArguments().get(0));
        UserInterface.data = (List<TableGroup>) bool.getArguments().get(1);
        if(!bool.getArguments().get(0).equals("Авторизация прошла успешно") && !bool.getArguments().get(0).equals("Регистрация прошла успешно")) {
            enterScene.exceptionMessage(bool);
        }
        return userdata;
    }

    public static List<TableGroup> getData() {
        return data;
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
                     org.island.details.exceptions.NoSuchCommandException e) {
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

    public static String getLogin() {
        return login;
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



    public static void setRegister(boolean register) {
        UserInterface.register = register;
    }

    public static void setPort(int port) {
        UserInterface.port = port;
    }

    public static void setPassword(String password) {
        UserInterface.password = password;
    }

    public static void setLogin(String login) {
        UserInterface.login = login;
    }
}