package org.example;


import org.example.exceptions.NoSuchCommandException;
import org.island.commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandsManager {
    private Map<String, Command> commandRegistry = new HashMap<>();
    private ArrayList<String> commandList = new ArrayList<>();
    private InputProcess inputProcess = new InputProcess();

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
    public Command commandForming(String s) throws NoSuchCommandException, org.island.details.exceptions.NoSuchCommandException {
        String[] str = parseCommand(s);
        Command command = getCommand(str[0].toLowerCase());
        command.clearArg();
        commandList.add(str[0]);
        Object[] args = Arrays.copyOfRange(str, 1, str.length);
        if(command.getClass() == InsertNull.class || command.getClass() == UpdateId.class || command.getClass() == Remove_greater.class){
            Object[] args1 = UserInterface.console.objectIdentity((String[]) args);
            command = command.clientExecute(args1);
        }
        else{
            command = command.clientExecute(args);
        }
        return command;
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
