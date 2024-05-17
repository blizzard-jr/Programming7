package org.example.island.commands;


import org.example.commandsManager.ExecuteManager;
import org.example.island.details.exceptions.NoSuchCommandException;

public class Exit extends Command{
    public Exit(){
        super("exit", "Завершить работу программы");
    }

    @Override
    public void execute(ExecuteManager manage){
        manage.executeExit();
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        return new Exit();
    }
}
