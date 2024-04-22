package org.example.island.commands;


import org.example.InputProcess;
import org.example.commandsManager.ExecuteManager;
import org.example.details.*;
import org.example.island.details.exceptions.NoSuchCommandException;
import org.example.island.object.StudyGroup;

public class Remove_greater extends Command{
    public Remove_greater(){
        super("remove_greater", "Удалить из коллекции все элементы, превышающие заданный");
        argumentCount = 3;
    }
    @Override
    public void execute(ExecuteManager manage) {
        manage.executeRemoveGreater(this.getArguments());
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        checkArgs(args);
        String name = args[0];
        long studentsCount;
        long shouldBeExpelled;
        try{
            studentsCount = Long.parseLong(args[1]);
            shouldBeExpelled = Long.parseLong(args[2]);
        }catch(NumberFormatException e){
            throw new NoSuchCommandException("Проблема с аргументами команды");
        }
        StudyGroup group = new InputProcess().studyGroupInit(name, studentsCount, shouldBeExpelled);
        this.setArguments(group);
        return this;
    }
}
