package org.example.island.commands;




import org.example.island.details.exceptions.NoSuchCommandException;

public class Remove_lower_key extends Command implements ChangingCollectionCommand{
    public Remove_lower_key(){
        super("remove_lower_key", "Удалить из коллекции все элементы, ключ которых меньше, чем заданный");
        argumentCount = 4;
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        checkArgs(args);
        int key;
        try{
            key = Integer.parseInt((String) args[0]);
        }catch(NumberFormatException e){
            throw new NoSuchCommandException("Неверное значение ключа");
        }
        this.setArguments(key);
        return this;
    }
}
