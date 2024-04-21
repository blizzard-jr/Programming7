package org.example.island.commands;


import org.example.exceptions.IllegalValueException;
import org.example.island.details.exceptions.NoSuchCommandException;
import org.example.island.object.StudyGroup;

import java.util.Arrays;
public class InsertNull extends Command{
    public InsertNull(){
        super("insert", "Добавить новый элемент с заданным ключом");
        argumentCount = 4;
    }
    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        checkArgs(args);
        Integer key = Integer.parseInt(args[0]);
        String[] fields = Arrays.copyOfRange(args, 1, args.length);
        String name = fields[0];
        long studentsCount;
        long studentsShouldBeExpelled;
        try{
            studentsCount = Long.parseLong(fields[1]);
            studentsShouldBeExpelled = Long.parseLong(fields[2]);
        } catch (NumberFormatException e) {
            throw new IllegalValueException("Введены некорректные значения для объекта");
        }
        if(fields[0].isEmpty() || studentsCount <=0 || studentsShouldBeExpelled <=0){
            throw new IllegalValueException("Введённые значения не валидны");
        }
        StudyGroup obj = process.studyGroupInit(name, studentsCount, studentsShouldBeExpelled);
        Object[] data = new Object[2];
        data[0] = key;
        data[1] = obj;
        this.setArguments(data);
        return this;
    }

    @Override
    public void execute() {
        manage.executeInsert(this.getArguments());
    }
}
