package org.example.island.commands;



import org.example.island.details.exceptions.NoSuchCommandException;
import org.example.island.object.FormOfEducation;

public class Count_less_than_form_of_education extends Command{
    public Count_less_than_form_of_education(){
        super("count_less_than_form_of_education", "Вывести количество элементов, значение поля formOfEducation которых меньше заданного");
        argumentCount = 3;
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        checkArgs(args);
        FormOfEducation form = FormOfEducation.getForm((String) args[0]);
        this.setArguments(form);
        return this;
    }
}
