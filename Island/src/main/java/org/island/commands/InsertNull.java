package org.island.commands;


import org.island.details.exceptions.NoSuchCommandException;

import java.util.ArrayList;
import java.util.Collections;


public class InsertNull extends Command implements ChangingCollectionCommand{
    public InsertNull(){
        super("insert", "Добавить новый элемент с заданным ключом");
        argumentCount = 5;
    }
    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        checkArgs(args);
        ArrayList<Object> arrayArg = new ArrayList<>();
        Collections.addAll(arrayArg, args);
        this.setArguments(arrayArg);
        return this;
    }
}
