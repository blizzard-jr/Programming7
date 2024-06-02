package org.example.island.commands;



import org.example.island.details.exceptions.NoSuchCommandException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Абстрактный класс для всех команд, доступных в приложении
 */

public abstract class Command implements Serializable {
    private ArrayList<Object> arguments = new ArrayList<>();
    protected int argumentCount = 2;
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
    public void clearArg(){
        this.arguments.clear();
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
    public void checkArgs(Object[] args) throws NoSuchCommandException {
        if(args.length + 3 != argumentCount){
            throw new NoSuchCommandException("Неверное количество аргументов команды");
        }
    }
    public String getDescription() {
        return description;
    }

    public abstract Command clientExecute(Object[] args) throws NoSuchCommandException;

    public int getArgumentCount() {
        return argumentCount;
    }
}
