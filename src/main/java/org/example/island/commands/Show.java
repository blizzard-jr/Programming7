package org.example.island.commands;



import org.example.island.details.exceptions.NoSuchCommandException;

public class Show extends Command{

    public Show(){
        super("show", "Вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }
    @Override
    public void execute() {
        manage.executeShow();
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        return this;
    }
}
