package org.example.island.commands;


import org.example.island.details.exceptions.NoSuchCommandException;

public class Message extends Command{


    public Message() {
        super("message", "Отправить текстовое сообщение");
        argumentCount = 4;
    }

    @Override
    public Command clientExecute(Object[] args) throws NoSuchCommandException {
        this.setArguments(args);
        return this;
    }

}
