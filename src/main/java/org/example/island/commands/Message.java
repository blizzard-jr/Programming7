package org.example.island.commands;

import org.example.commandsManager.ExecuteManager;
import org.example.island.details.exceptions.NoSuchCommandException;

public class Message extends Command{


    public Message() {
        super("message", "Отправить текстовое сообщение");
        argumentCount = 1;
    }

    @Override
    public void execute(ExecuteManager manage) {
        Object[] data = getArguments();
        String str = (String) data[0];
        manage.executeMessage(str);
    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        this.setArguments(args);
        return this;
    }
}
