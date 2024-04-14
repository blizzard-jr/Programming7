package org.example.commands;



public class Clear extends Command{
    public Clear(){
        super("clear", "Очистить коллекцию");
    }
    @Override
    public void execute(String[] args) throws NoSuchCommandException {
        checkArgs(args);
        StorageOfManagers.collectionManager.clear();
        System.out.println("Коллекция очищена");
    }
}
