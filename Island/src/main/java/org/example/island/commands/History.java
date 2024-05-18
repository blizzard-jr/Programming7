package org.example.island.commands;

import org.example.island.details.exceptions.NoSuchCommandException;




public class History extends Command{
    public History(){
        super("history","Вывести последние 14 команд (без их аргументов)");
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        return this;
    }
}
