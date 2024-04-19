package org.example.server.details;


import org.example.client.UserInterface;
import org.example.server.commandsManager.CommandsManager;
import org.example.server.fileSystem.FileSystem;

/**
 * Класс для хранения менеджеров, позволяет избежать использования синглтонов
 */

public class StorageOfManagers {
    private final Storage storage = new Storage();
    private CollectionManager collectionManager;
    private UserInterface userInterface;
    private FileSystem fileSystem;
    private CommandsManager commandsManager;
    public StorageOfManagers(CollectionManager collectionManager, UserInterface userInterface, FileSystem fileSystem, CommandsManager commandsManager, Storage storage){
        this.collectionManager = collectionManager;
        this.userInterface = userInterface;
        this.fileSystem = fileSystem;
        this.commandsManager = commandsManager;
        this.storage = storage;
    }

    public Storage getStorage() {
        return storage;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public UserInterface getUserInterface() {
        return userInterface;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public CommandsManager getCommandsManager() {
        return commandsManager;
    }
}
