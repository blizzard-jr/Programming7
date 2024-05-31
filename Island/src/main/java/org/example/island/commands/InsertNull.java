package org.example.island.commands;


import org.example.island.details.exceptions.NoSuchCommandException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class InsertNull extends Command{
    public InsertNull(){
        super("insert", "Добавить новый элемент с заданным ключом");
        argumentCount = 4;
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
