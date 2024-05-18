package org.example.island.commands;




import org.example.island.details.exceptions.NoSuchCommandException;

import java.io.Serializable;

public class Show extends Command implements Serializable {

    public Show(){
        super("show", "Вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        return this;
    }
}
