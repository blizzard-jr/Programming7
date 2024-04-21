package org.example.island.commands;


import org.example.CommandsManager;

import org.example.island.details.exceptions.NoSuchCommandException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Help extends Command{
    public Help(){
        super("help", "Вывести справку по доступным командам");
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        Map<String, Command> map = new CommandsManager().getCommandsReg();
        for(Command command : map.values()){
            System.out.println(command.getName() + " - " + command.getDescription());
        }
        return null;
    }

    @Override
    public void execute() {

    }
}
