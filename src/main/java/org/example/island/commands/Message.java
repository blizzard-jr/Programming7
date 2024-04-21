package org.example.island.commands;

import org.example.island.details.exceptions.NoSuchCommandException;

public class Message extends Command{


    public Message() {
        super("message", "Отправить текстовое сообщение");
        argumentCount = 1;
    }

    @Override
    public void execute() {

    }

    @Override
    public Command clientExecute(String[] args) throws NoSuchCommandException {
        this.setArguments(args);
        return this;
    }
}
