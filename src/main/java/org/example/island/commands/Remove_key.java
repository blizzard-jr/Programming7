package org.example.island.commands;



import org.example.commandsManager.ExecuteManager;
import org.example.exceptions.IllegalValueException;
import org.example.island.details.exceptions.NoSuchCommandException;

public class Remove_key extends Command{
    public Remove_key(){
        super("remove", "Удалить элемент из коллекции по его ключу");
        argumentCount = 1;
    }
    @Override
    public void execute(ExecuteManager manage) {
        manage.executeRemove(this.getArguments());
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        checkArgs(args);
        int key;
        try{
            key = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            throw new IllegalValueException("Ошибка в аргументе команды");
        }
        this.setArguments(key);
        return this;
    }
}
