package org.example.island.commands;


import org.example.island.details.exceptions.NoSuchCommandException;

public class Clear extends Command{
    public Clear(){
        super("clear", "Очистить коллекцию");
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        checkArgs(args);
        return this;
    }
}