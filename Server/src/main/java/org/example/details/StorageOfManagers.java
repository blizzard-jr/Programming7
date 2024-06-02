
package org.example.details;


import org.example.commandsManager.DataBaseManager;
import org.example.answers.AnswerManager;
import org.example.commandsManager.ExecuteManager;
import org.example.fileSystem.FileSystem;


/**
 * Класс для хранения менеджеров, позволяет избежать использования синглтонов
 */

public class StorageOfManagers {
    public static Storage storage;
    public static DataBaseManager dBManager;
    public static ExecuteManager executeManager;
    public static AnswerManager answerManager;
    public static FileSystem fileSystem;

    public static void setFileSystem(FileSystem fileSystem) {
        StorageOfManagers.fileSystem = fileSystem;
    }

    public static void setAnswerManager(AnswerManager answerManager) {
        StorageOfManagers.answerManager = answerManager;
    }

    public static void setExecuteManager(ExecuteManager executeManager) {
        StorageOfManagers.executeManager = executeManager;
    }
    public static void setDataBaseManager(DataBaseManager dataBaseManager) {
        StorageOfManagers.dBManager = dataBaseManager;
    }

    public static void setStorage(Storage storage) {
        StorageOfManagers.storage = storage;
    }
}



