
package org.example.details;


import org.example.answers.AnswerManager;
import org.example.commandsManager.ExecuteManager;
import org.example.fileSystem.FileSystem;
import org.example.requests.RequestsManager;


/**
 * Класс для хранения менеджеров, позволяет избежать использования синглтонов
 */

public class StorageOfManagers {
    public static  Storage storage;
    public static FileSystem fileSystem;
    public  static ExecuteManager executeManager;

    public StorageOfManagers(Storage storage, FileSystem fileSystem, ExecuteManager executeManager){
        StorageOfManagers.storage = storage;
        StorageOfManagers.fileSystem = fileSystem;
        StorageOfManagers.executeManager = executeManager;
    }
}



