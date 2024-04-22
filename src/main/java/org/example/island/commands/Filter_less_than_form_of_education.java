package org.example.island.commands;



import org.example.commandsManager.ExecuteManager;
import org.example.island.details.exceptions.NoSuchCommandException;
import org.example.island.object.FormOfEducation;

public class Filter_less_than_form_of_education extends Command {
    public Filter_less_than_form_of_education(){
        super("filter_less_than_form_of_education", "Вывести элементы, значение поля formOfEducation которых меньше заданного");
        argumentCount = 1;
    }


    @Override
    public void execute(ExecuteManager manage) {
        manage.executeFilterEdu(this.getArguments());
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        checkArgs(args);
        FormOfEducation form = FormOfEducation.getForm(args[0]);
        this.setArguments(form);
        return this;
    }
}
