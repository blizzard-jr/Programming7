package org.example.island.commands;


import org.example.island.details.exceptions.NoSuchCommandException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class Execute_script extends Command{
    public static Set<String> files = new HashSet<String>();


    public Execute_script(){
        super("execute_script", "Считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме");
        argumentCount = 1;
    }

    @Override
    public void execute() {
        manage.executeScript(this.getArguments());
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        checkArgs(args);
        FileInputStream stream;
        try {
            stream = new FileInputStream(args[0]);
        } catch (FileNotFoundException e) {
            throw new NoSuchCommandException("Ошибка в имени файла");
        }
        this.setArguments(args[0]);
        return this;
    }
}


