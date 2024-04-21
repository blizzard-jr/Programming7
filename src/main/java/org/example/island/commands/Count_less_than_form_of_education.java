package org.example.island.commands;



import org.example.island.details.exceptions.NoSuchCommandException;
import org.example.island.object.FormOfEducation;

public class Count_less_than_form_of_education extends Command{
    public Count_less_than_form_of_education(){
        super("count_less_than_form_of_education", "Вывести количество элементов, значение поля formOfEducation которых меньше заданного");
        argumentCount = 1;
    }
    @Override
    public void execute() {
        manage.executeCountEdu(this.getArguments());
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        checkArgs(args);
        FormOfEducation form = FormOfEducation.getForm(args[0]);
        this.setArguments(form);
        return this;
    }
}
