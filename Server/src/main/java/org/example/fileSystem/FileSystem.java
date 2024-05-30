package org.example.fileSystem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.details.StorageOfManagers;
import org.example.island.object.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Класс отвечает за работу с файлами
 */
public class FileSystem {
    private String fileName = "";
    private Scanner scanner = new Scanner(System.in);
    private Logger logger = LoggerFactory.getLogger(FileSystem.class);

    /**
     * Метод десериализует информацию из файла в Map POJO
     * @param file
     * @return map
     * @throws IOException
     */
    public LinkedHashMap<Integer, StudyGroup> parseToList(String file) throws IOException {
        FileInputStream f = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(f);
        ObjectMapper o = new ObjectMapper().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LinkedHashMap<Integer, StudyGroup> map_one = o.readValue(input, new TypeReference<>(){});
        map_one.entrySet().stream().sorted();
        fileInit(file);
        return map_one;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * Метод для запоминания пути к файлу с которым работает коллекция
     * @param s
     */
    public void fileInit(String s){
        this.fileName = s;
    }

    /**
     * Метод сериализует данные из коллекции в файл
     * @param map
     */
    public String parseToFile(LinkedHashMap<Integer, StudyGroup> map)  {
        FileOutputStream f = null;
        try {
            f = new FileOutputStream(fileName);
        } catch (FileNotFoundException | NullPointerException e) {
            return "Сохранение коллекции не произошло";
        }
        OutputStreamWriter writer = new OutputStreamWriter(f);
        ObjectMapper o = new ObjectMapper();
        o.registerModules(new JavaTimeModule());
        o.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, SerializationFeature.INDENT_OUTPUT);
        try {
            o.writeValue(writer, map);
            return "Коллекция сохранена в файл";
        } catch (IOException e) {
            return "Не удалось сохранить коллекцию в файл";
        }
    }

    /**
     * Метод для парсинга скрипта из файла
     * @param stream
     */
    public String parseScript(FileInputStream stream)  {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            while (reader.ready()) {
                String string = reader.readLine();
                if(string.split(" ")[0].equals("insert") || string.split(" ")[0].equals("update")){
                    String[] str = Arrays.copyOfRange(string.split(" "), 1, string.split(" ").length);
                    ArrayList<String> data = new ArrayList<>(Arrays.asList(str));
                    for (int i = 0; i < 12; i++) {
                        data.add(reader.readLine());
                    }
                    if(string.split(" ")[0].equals("insert")){
                        StorageOfManagers.executeManager.insertFormScript(data);
                    }
                    else if(string.split(" ")[0].equals("update")){
                        StorageOfManagers.executeManager.updateFromScript(data);
                    }

                }
                else if(string.isEmpty()){
                    break;
                }
                else{
                    StorageOfManagers.executeManager.commandExecute(string);
                }
            }
            return "Выполнение скрипта завершено";
        }catch(IOException e){
            return ("Проблема с парсингом файла или команда не найдена");
        }
    }
}
