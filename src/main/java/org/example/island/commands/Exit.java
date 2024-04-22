package org.example.island.commands;


import org.example.commandsManager.ExecuteManager;
import org.example.island.details.exceptions.NoSuchCommandException;

public class Exit extends Command{
    public Exit(){
        super("exit", "Завершить программу (без сохранения в файл)");
    }

    @Override
    public void execute(ExecuteManager manage){
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        System.out.println("I`ll be back, babe :)");
        System.exit(0);
        return null;
    }
}
