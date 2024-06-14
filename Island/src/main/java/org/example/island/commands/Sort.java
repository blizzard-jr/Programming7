package org.example.island.commands;

import org.example.island.details.exceptions.NoSuchCommandException;

public class Sort extends Command{

    public Sort() {
        super("sort", "Сортировка upOrDown");
        argumentCount = 4;
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        return null;
    }
}
