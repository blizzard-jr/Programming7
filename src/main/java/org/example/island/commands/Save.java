package org.example.island.commands;



import org.example.commandsManager.ExecuteManager;
import org.example.island.details.exceptions.NoSuchCommandException;

public class Save extends Command{
    public Save(){
        super("save", "Сохранить коллекцию в файл");
    }
    @Override
    public void execute(ExecuteManager manage) {
        manage.executeSave();
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        return null;
    }
}
