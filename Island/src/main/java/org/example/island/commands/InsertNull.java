package org.example.island.commands;


import org.example.island.details.exceptions.NoSuchCommandException;



public class InsertNull extends Command{
    public InsertNull(){
        super("insert", "Добавить новый элемент с заданным ключом");
        argumentCount = 2;
    }
    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        checkArgs(args);
        this.setArguments(args);
        return this;
    }
}
