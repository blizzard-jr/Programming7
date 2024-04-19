package org.example.client;

import org.example.commands.*;
import org.example.server.details.Serialization;
import org.example.server.exceptions.NoSuchCommandException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandsManager {
    private Map<String, Command> commandRegistry = new HashMap<>();
    private ArrayList<String> commandList = new ArrayList<>();

    /**
     * Конструктор инициализирующий все команды приложения
     */

    public CommandsManager(){
        addCommand(new Show());
        addCommand(new Info());
        addCommand(new InsertNull());
        addCommand(new UpdateId());
        addCommand(new Remove_key());
        addCommand(new Clear());
        addCommand(new Save());
        addCommand(new Execute_script());
        addCommand(new Remove_greater());
        addCommand(new Remove_lower_key());
        addCommand(new History());
        addCommand(new Count_less_than_form_of_education());
        addCommand(new Filter_by_students_count());
        addCommand(new Filter_less_than_form_of_education());
        addCommand(new Exit());
        addCommand(new Help());
    }
    public void addCommand(Command cmd){
        commandRegistry.put(cmd.getName(), cmd);
    }
    public ByteBuffer commandForming(String s) throws NoSuchCommandException {
        String[] str = parseCommand(s);
        Command command = getCommand(str[0].toLowerCase());
        commandList.add(str[0]);
        String[] args = Arrays.copyOfRange(str, 1, str.length);
        HashMap<Command, String[]> parcel = new HashMap<>();
        ByteBuffer buffer = ByteBuffer.wrap(Serialization.SerializeObject(command));
        buffer.put(Serialization.SerializeObject(args));
        return buffer;
    }
    /**
     * Метод принимает строку, переданную через консоль, и разбивает её, позволяя разделить имя команды и аргументы
     * @param s
     * @return String[]
     */

    public String[] parseCommand(String s){
        return s.split(" ");
    }
    /**
     * Метод принимает строку и возвращает команду, если такое имя есть в списке
     * @param s
     * @return Command
     * @throws NoSuchCommandException
     */

    public Command getCommand(String s) throws NoSuchCommandException {
        if(!commandRegistry.containsKey(s)){
            throw new NoSuchCommandException("Команда не найдена, повторите ввод");
        }
        return commandRegistry.getOrDefault(s, null);
    }
}
