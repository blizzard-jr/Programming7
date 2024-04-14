package org.example.commands;

import exception.NoSuchCommandException;
import org.example.details.StorageOfManagers;

public class Show extends Command{

    public Show(){
        super("show", "Вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }
    @Override
    public void execute(String[] args) throws NoSuchCommandException {
        checkArgs(args);
        StorageOfManagers.collectionManager.stringValue();
    }
}
