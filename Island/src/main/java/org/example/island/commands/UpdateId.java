package org.example.island.commands;




import org.example.island.details.exceptions.NoSuchCommandException;


public class UpdateId extends Command{
    public UpdateId(){
        super("update", "Обновить значение элемента коллекции, id которого равен заданному");
        argumentCount = 4;
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        checkArgs(args);
        this.setArguments(args);
        return this;
    }
}
