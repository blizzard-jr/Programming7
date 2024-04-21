package org.example.island.commands;



import org.example.island.details.exceptions.NoSuchCommandException;

public class Info extends Command{
    public Info() {
        super("info", "Вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
    }
    @Override
    public void execute() {
        manage.executeInfo();
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        return this;
    }
}
