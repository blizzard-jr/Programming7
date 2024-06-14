package org.example.island.commands;

import org.example.island.details.exceptions.NoSuchCommandException;

public class Filter extends Command implements ChangingCollectionCommand{

    public Filter() {
        super("filter", "Фильтрация по значению");
        argumentCount = 4;
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        return null;
    }
}
