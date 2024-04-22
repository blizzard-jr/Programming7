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
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        ArrayList<String> list = new CommandsManager().getCommandList();
        if(list.size() < 14){
            System.out.println("Количества исполненных команд не достаточно для вывода истории");
        }
        else{
            for (int i = 0; i < 14; i++) {
                System.out.println(list.get(i));
            }
        }
        return this;
    }
}
