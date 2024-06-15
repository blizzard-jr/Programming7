package org.island.commands;



import org.island.details.exceptions.NoSuchCommandException;

public class Exit extends Command{
    public Exit(){
        super("exit", "Завершить работу программы");
    }


    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        return new Exit();
    }
}
