package org.island.commands;




import org.island.details.exceptions.NoSuchCommandException;

public class Save extends Command{
    public Save(){
        super("save", "Сохранить коллекцию в файл");
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        return null;
    }
}
