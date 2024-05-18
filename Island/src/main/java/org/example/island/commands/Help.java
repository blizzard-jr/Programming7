package org.example.island.commands;





import org.example.island.details.exceptions.NoSuchCommandException;



public class Help extends Command{
    public Help(){
        super("help", "Вывести справку по доступным командам");
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        return this;
    }
}
