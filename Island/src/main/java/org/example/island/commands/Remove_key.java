package org.example.island.commands;




import org.example.island.details.exceptions.IllegalValueException;
import org.example.island.details.exceptions.NoSuchCommandException;

public class Remove_key extends Command{
    public Remove_key(){
        super("remove", "Удалить элемент из коллекции по его ключу");
        argumentCount = 1;
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        checkArgs(args);
        int key;
        try{
            key = Integer.parseInt((String) args[0]);
        }catch (NumberFormatException e){
            throw new IllegalValueException("Ошибка в аргументе команды");
        }
        this.setArguments(key);
        return this;
    }
}
