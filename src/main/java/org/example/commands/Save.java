package org.example.commands;

import exception.NoSuchCommandException;
import org.example.details.StorageOfManagers;
public class Save extends Command{
    public Save(){
        super("save", "Сохранить коллекцию в файл");
    }
    @Override
    public void execute(String[] args) throws NoSuchCommandException {
        checkArgs(args);
        StorageOfManagers.collectionManager.save();
        System.out.println("Коллекция сохранена в файл");
    }
}
