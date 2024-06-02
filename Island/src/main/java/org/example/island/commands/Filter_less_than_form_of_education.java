package org.example.island.commands;




import org.example.island.details.exceptions.NoSuchCommandException;
import org.example.island.object.FormOfEducation;

public class Filter_less_than_form_of_education extends Command {
    public Filter_less_than_form_of_education(){
        super("filter_less_than_form_of_education", "Вывести элементы, значение поля formOfEducation которых меньше заданного");
        argumentCount = 4;
    }


    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        checkArgs(args);
        FormOfEducation form = FormOfEducation.getForm((String) args[0]);
        this.setArguments(form);
        return this;
    }
}
