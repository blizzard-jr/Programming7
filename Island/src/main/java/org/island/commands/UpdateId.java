package org.island.commands;




import org.island.details.exceptions.NoSuchCommandException;


public class UpdateId extends Command implements ChangingCollectionCommand{
    public UpdateId(){
        super("update", "Обновить значение элемента коллекции, id которого равен заданному");
        argumentCount = 5;
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        checkArgs(args);
        this.setArguments(args);
        return this;
    }
}
