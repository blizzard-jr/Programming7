package org.example.island.commands;



import org.example.commandsManager.ExecuteManager;
import org.example.island.details.exceptions.NoSuchCommandException;

public class Remove_lower_key extends Command{
    public Remove_lower_key(){
        super("remove_lower_key", "Удалить из коллекции все элементы, ключ которых меньше, чем заданный");
        argumentCount = 1;
    }
    @Override
    public void execute(ExecuteManager manage)  {
        manage.executeRemoveLower(this.getArguments());
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        checkArgs(args);
        int key;
        try{
            key = Integer.parseInt(args[0]);
        }catch(NumberFormatException e){
            throw new NoSuchCommandException("Неверное значение ключа");
        }
        this.setArguments(key);
        return this;
    }
}
