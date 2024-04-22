package org.example.island.commands;



import org.example.commandsManager.ExecuteManager;
import org.example.island.details.exceptions.NoSuchCommandException;

public class Filter_by_students_count extends Command{
    public Filter_by_students_count(){
        super("filter_by_students_count", "Вывести элементы, значение поля studentsCount которых равно заданному");
        argumentCount = 1;
    }

    @Override
    public void execute(ExecuteManager manage) {
        manage.executeFilterStudentsCount(this.getArguments());
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        checkArgs(args);
        long studentCount;
        try{
            studentCount = Long.parseLong(args[0]);
        }catch(NumberFormatException e){
            throw new NoSuchCommandException("Проблема в аргументе команды");
        }
        this.setArguments(args[0]);
        return this;
    }
}
