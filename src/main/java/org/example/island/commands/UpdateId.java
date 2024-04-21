package org.example.island.commands;



import org.example.exceptions.IllegalValueException;
import org.example.island.details.exceptions.NoSuchCommandException;
import org.example.island.object.StudyGroup;

public class UpdateId extends Command{
    public UpdateId(){
        super("update", "Обновить значение элемента коллекции, id которого равен заданному");
        argumentCount = 4;
    }
    @Override
    public void execute() {
        manage.executeUpdate(this.getArguments());
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        checkArgs(args);
        String name = args[1];
        int id;
        long studentsCount;
        long studentsShouldBeExpelled;
        try{
            id = Integer.parseInt(args[0]);
            studentsCount = Long.parseLong(args[2]);
            studentsShouldBeExpelled = Long.parseLong(args[3]);
        } catch (NumberFormatException e) {
            throw new IllegalValueException("Введены некорректные значения для объекта");
        }
        if(name.isEmpty() || studentsCount <=0 || studentsShouldBeExpelled <=0){
            throw new IllegalValueException("Введённые значения не валидны");
        }
        StudyGroup el = process.studyGroupInit(name, studentsCount,studentsShouldBeExpelled);
        Object[] data = new Object[2];
        data[0] = id;
        data[1] = el;
        this.setArguments(data);
        return this;
    }
}
