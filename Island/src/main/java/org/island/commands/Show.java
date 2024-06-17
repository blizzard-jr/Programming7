package org.island.commands;




import org.island.details.exceptions.NoSuchCommandException;

import java.io.Serializable;

public class Show extends Command implements Serializable, ChangingCollectionCommand {
    public Show(){
        super("show", "Вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        return this;
    }
}
