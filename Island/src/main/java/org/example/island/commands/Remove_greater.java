package org.example.island.commands;




import org.example.island.details.exceptions.NoSuchCommandException;
import org.example.island.object.StudyGroup;

public class Remove_greater extends Command{
    public Remove_greater(){
        super("remove_greater", "Удалить из коллекции все элементы, превышающие заданный");
        argumentCount = 5;
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        StudyGroup group = (StudyGroup) args[0];
        this.setArguments(group);
        return this;
    }
}
