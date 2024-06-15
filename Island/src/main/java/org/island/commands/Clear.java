package org.island.commands;


import org.island.details.exceptions.NoSuchCommandException;

public class Clear extends Command implements ChangingCollectionCommand{
    public Clear(){
        super("clear", "Очистить коллекцию");
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        checkArgs(args);
        return this;
    }
}