package org.example.island.commands;



import org.example.island.details.exceptions.NoSuchCommandException;

public class Exit extends Command{
    public Exit(){
        super("exit", "Завершить работу программы");
    }


    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        return new Exit();
    }
}
