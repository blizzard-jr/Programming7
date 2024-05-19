
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

    public static void setExecuteManager(ExecuteManager executeManager) {
        StorageOfManagers.executeManager = executeManager;
    }

    public static void setFileSystem(FileSystem fileSystem) {
        StorageOfManagers.fileSystem = fileSystem;
    }

    public static void setStorage(Storage storage) {
        StorageOfManagers.storage = storage;
    }
}



