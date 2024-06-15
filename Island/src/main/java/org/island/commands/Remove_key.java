package org.island.commands;




import org.island.details.exceptions.IllegalValueException;
import org.island.details.exceptions.NoSuchCommandException;

public class Remove_key extends Command implements ChangingCollectionCommand{
    public Remove_key(){
        super("remove", "Удалить элемент из коллекции по его ключу");
        argumentCount = 4;
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
