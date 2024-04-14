package org.example.server.details;


import org.example.client.UserInterface;
import org.example.server.commandsManager.CommandsManager;
import org.example.server.fileSystem.FileSystem;

/**
 * Класс для хранения менеджеров, позволяет избежать использования синглтонов
 */

public class StorageOfManagers {
    public static Storage storage;
    public static CollectionManager collectionManager;
    public static UserInterface userInterface;
    public static FileSystem fileSystem;
    public static CommandsManager commandsManager;
    public StorageOfManagers(CollectionManager collectionManager, UserInterface userInterface, FileSystem fileSystem, CommandsManager commandsManager, Storage storage){
        StorageOfManagers.collectionManager = collectionManager;
        StorageOfManagers.userInterface = userInterface;
        StorageOfManagers.fileSystem = fileSystem;
        StorageOfManagers.commandsManager = commandsManager;
        StorageOfManagers.storage = storage;
    }

//Сделаать методы getmanage
}
