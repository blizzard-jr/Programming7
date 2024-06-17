package org.island.commands;

import org.island.details.exceptions.NoSuchCommandException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class Execute_script extends Command{
    public static Set<String> files = new HashSet<String>();


    public Execute_script(){
        super("execute_script", "Считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме");
        argumentCount = 4;
    }


    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        checkArgs(args);
        FileInputStream stream;
        try {
            stream = new FileInputStream((String) args[0]);
        } catch (FileNotFoundException e) {
            throw new NoSuchCommandException("Ошибка в имени файла");
        }
        this.setArguments(args[0]);
        return this;
    }
}


