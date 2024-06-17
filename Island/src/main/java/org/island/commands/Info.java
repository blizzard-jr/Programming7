package org.island.commands;




import org.island.details.exceptions.NoSuchCommandException;

public class Info extends Command{
    public Info() {
        super("info", "Вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        return this;
    }
}
