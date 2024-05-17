package org.example.island.commands;
import org.example.CommandsManager;

import org.example.commandsManager.ExecuteManager;
import org.example.island.details.exceptions.NoSuchCommandException;

import java.util.ArrayList;



public class History extends Command{
    public History(){
        super("history","Вывести последние 14 команд (без их аргументов)");
    }
    @Override
    public void execute(ExecuteManager manage) {
        manage.executeHistory();
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        return this;
    }
}
