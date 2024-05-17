package org.example.island.commands;


import org.example.commandsManager.ExecuteManager;
import org.example.island.details.exceptions.NoSuchCommandException;
import org.example.InputProcess;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Абстрактный класс для всех команд, доступных в приложении
 */

public abstract class Command implements Serializable {
    private ArrayList<Object> arguments = new ArrayList<>();
    protected int argumentCount = 0;
    private String name;
    private String description;

    /**
     * Конструктор класса, принимает имя и описание команды
     * @param name
     * @param description
     */
    public Command(String name, String description){
        this.name = name;
        this.description = description;
    }

    public void setArguments(ArrayList<Object> arguments) {
        this.arguments = arguments;
    }
    public void setArguments(Object[] data){
        this.arguments.addAll(Arrays.asList(data));
    }
    public void setArguments(Object str){
        this.arguments.add(str);

    }

    public ArrayList<Object> getArguments() {
        return arguments;
    }
    /*
    public int getArgumentCount(){
        return this.argumentCount;
    }
     */
    public String getName() {
        return name;
    }

    /**
     * Метод проверяет количество аргументов команды
     * @param args
     * @throws NoSuchCommandException
     */
    public void checkArgs(String[] args) throws NoSuchCommandException {
        if(args.length != argumentCount){
            throw new NoSuchCommandException("Неверное количество аргументов команды");
        }
    }
    public String getDescription() {
        return description;
    }

    /**
     * Абстрактный метод, отвечающий за исполнение всех команд
     * @throws NoSuchCommandException
     */
    public abstract void execute(ExecuteManager manage);

    public abstract Command clientExecute(String[] args) throws NoSuchCommandException;
}
