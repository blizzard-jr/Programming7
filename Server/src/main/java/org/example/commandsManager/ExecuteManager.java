package org.example.commandsManager;

import org.example.CommandsManager;
import org.example.answers.AnswerManager;
import org.example.details.Storage;

import org.example.details.StorageOfManagers;
import org.example.exceptions.IllegalValueException;
import org.example.fileSystem.FileSystem;
import org.example.island.commands.*;
import org.example.island.details.exceptions.NoSuchCommandException;
import org.example.island.object.*;
import org.example.requests.RequestsManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;


public class ExecuteManager {
    private Map<String, Command> commandRegistry = new HashMap<>();
    private ArrayList<String> commandList = new ArrayList<>();

    public ExecuteManager(){
        addCommand(new Show());
        addCommand(new Info());
        addCommand(new InsertNull());
        addCommand(new UpdateId());
        addCommand(new Remove_key());
        addCommand(new Clear());
        addCommand(new Execute_script());
        addCommand(new Remove_greater());
        addCommand(new Remove_lower_key());
        addCommand(new History());
        addCommand(new Count_less_than_form_of_education());
        addCommand(new Filter_by_students_count());
        addCommand(new Filter_less_than_form_of_education());
        addCommand(new Exit());
        addCommand(new Help());
        addCommand(new Message());
        addCommand(new Save());
    }

    public Map<String, Command> getCommandRegistry() {
        return commandRegistry;
    }

    public void executeHelp(){
        Map<String, Command> map = getCommandRegistry();
        for(Command command : map.values()){
            RequestsManager.manager.answerForming(command.getName() + " - " + command.getDescription());
        }
    }

    public ArrayList<String> getCommandList() {
        return commandList;
    }

    public void executeHistory(){
        ArrayList<String> list = getCommandList();
        if(list.size() < 14){
            RequestsManager.manager.answerForming("Количества исполненных команд не достаточно для вывода истории");
        }
        else{
            for (int i = 0; i < 14; i++) {
                RequestsManager.manager.answerForming(list.get(i));
            }
        }
    }
    public void executeCLear(){
        String answer = StorageOfManagers.storage.clear();
        RequestsManager.manager.answerForming(answer);
    }
    public void executeCountEdu(ArrayList<Object> args){
        FormOfEducation form = (FormOfEducation) args.get(0);
        int count = 0;
        for(StudyGroup group : StorageOfManagers.storage.getValues()){
            if(group.getFormOfEducation().compareTo(form) < 0){
                count+=1;
            }
        }
        String answer = "Поле FormOfEducation меньше заданного вами значения у " + count + " элементов";
        RequestsManager.manager.answerForming(answer);
    }

    public void executeScript(ArrayList<Object> args){
        FileInputStream stream = null;
        try {
            stream = new FileInputStream((String) args.get(0));
        } catch (FileNotFoundException e) {
            RequestsManager.manager.answerForming(e.getMessage());
            return;
        }
        if(!Execute_script.files.contains((String) args.get(0))){
            Execute_script.files.add((String) args.get(0));
            StorageOfManagers.fileSystem.parseScript(stream);
            RequestsManager.manager.answerForming("Выполнение скрипта завершено");
        }
        else{
            RequestsManager.manager.answerForming("Не не, слишком бесконечно получается");
        }
    }
    public void executeFilterStudentsCount(ArrayList<Object> args){
        long studentCount = 0;
        try{
            studentCount = Long.parseLong((String) args.get(0));
        }catch(NumberFormatException e){
            RequestsManager.manager.answerForming(e.getMessage());
        }
        int flag = 0;
        List<String> list = new ArrayList<>();
        for(StudyGroup el : StorageOfManagers.storage.getValues()){
            if(el.getStudentsCount() == studentCount){
                flag+=1;
                list.add(el.toString());
            }
        }
        if(flag == 0){
            RequestsManager.manager.answerForming("В ходе выполнения программы совпадений не выявлено");
        }
        else{
            RequestsManager.manager.answerForming(list.toArray());
        }
    }
    public void executeFilterEdu(ArrayList<Object> args){
        FormOfEducation form;
        if(args.get(0).getClass() == String.class){
            form = FormOfEducation.getForm((String) args.get(0));
        }
        else{
            form = (FormOfEducation) args.get(0);
        }
        boolean flag = false;
        List<String> list = new ArrayList<>();
        for(StudyGroup group : StorageOfManagers.storage.getValues()){
            if(group.getFormOfEducation().compareTo(form) < 0){
                flag = true;
                list.add(group.toString());
            }
        }
        if(!flag){
            RequestsManager.manager.answerForming("Элементов, у которых значение поля FormOfEducation меньше заданного вами значения не нашлось");
        }
        else{
            RequestsManager.manager.answerForming(list.toArray());
        }
    }
    public void executeInfo(){
        RequestsManager.manager.answerForming(StorageOfManagers.storage.toString());
    }
    public void executeInsert(ArrayList<Object> args){
        StudyGroup obj = (StudyGroup) args.get(1);
        Integer key = (Integer) args.get(0);
        StorageOfManagers.storage.putWithKey(key, obj);
        StorageOfManagers.storage.putMapKeys(key, obj.getId());
        RequestsManager.manager.answerForming("Объект успешно добавлен");
    }

    public void executeRemoveGreater(ArrayList<Object> args){
        StudyGroup el = (StudyGroup) args.get(0);
        ArrayList<StudyGroup> keys = new ArrayList<>();
        for(StudyGroup element : StorageOfManagers.storage.getValues()){
            if(el.compareTo(element) > 0){
                keys.add(element);
            }
        }
        int count = 0;
        for(StudyGroup groups : keys){
            if(StorageOfManagers.storage.removeElement(StorageOfManagers.storage.findKey(groups.getId()))){
                count+=1;
            }
        }
        RequestsManager.manager.answerForming("В ходе исполнения команды было удалено " + count + " объектов");

    }

    public void executeRemove(ArrayList<Object> args){
        int key = 0;
        try{
            key = (Integer) args.get(0);
        }catch (NumberFormatException e){
            RequestsManager.manager.answerForming(e.getMessage());
        }
        if(StorageOfManagers.storage.removeElement(key)){
            RequestsManager.manager.answerForming("Объект успешно удалён");
        }
        else{
            RequestsManager.manager.answerForming("Объект, соответсвующий введённому вами ключу в коллекции не найден");
        }
    }
    public void executeRemoveLower(ArrayList<Object> args){
        int key = 0;
        try{
            key = Integer.parseInt((String) args.get(0));
        }catch(NumberFormatException e){
            RequestsManager.manager.answerForming(e.getMessage());
        }
        ArrayList<Integer> keys = new ArrayList<>();
        for(Integer key_i : StorageOfManagers.storage.getKeys()){
            if(key > key_i){
                keys.add(key_i);
            }
        }
        if(keys.size() == StorageOfManagers.storage.getSize()){
            RequestsManager.manager.answerForming("Введённый вами ключ меньше всех ключей, что есть в коллекции, удаление элементов не было произведено");
        }
        else{
            int count = 0;
            for (Integer integer : keys) {
                if(StorageOfManagers.storage.removeElement(integer)){
                    count += 1;
                }
            }
            RequestsManager.manager.answerForming("В ходе исполнения команды было удалено " + count + " объектов");
        }
    }
    public void executeSave(){
        StorageOfManagers.fileSystem.parseToFile(StorageOfManagers.storage.getMap());
    }

    public void executeMessage(String data){
        if(data.equals("Пустая коллекция")){
            StorageOfManagers.storage.mapInit(new LinkedHashMap<>());
            RequestsManager.manager.answerForming("Коллекция инициализирована");
        }
        else{
            try {
                StorageOfManagers.storage.mapInit(StorageOfManagers.fileSystem.parseToList(data));
                RequestsManager.manager.answerForming("Коллекция инициализирована");
            } catch (IOException e) {
                RequestsManager.manager.answerForming(e.getMessage());
            }
        }
    }
    public void executeShow(){
        List<String> list = new ArrayList<>();
        Collection<StudyGroup> s = StorageOfManagers.storage.getValue();
        for (StudyGroup group : s) {
            list.add(group.toString());
        }
        if(!list.isEmpty()){
            RequestsManager.manager.answerForming(list.toArray());
        }
        else{
            RequestsManager.manager.answerForming("В коллекции пока нет элементов");
        }

    }
    public void executeUpdate(ArrayList<Object> args){
        StudyGroup element = (StudyGroup) args.get(1);
        Integer id = (Integer) args.get(0);
        element.setId(id);
        StorageOfManagers.storage.replaceElement(id, element);
        RequestsManager.manager.answerForming("Объект успешно заменён");
    }
    public void insertFormScript(ArrayList<String> args){
        try{
            int key = Integer.parseInt(args.get(0));
            long studentsCount = Long.parseLong(args.get(2));
            long shouldBeExpelled = Long.parseLong(args.get(3));
            FormOfEducation form = FormOfEducation.getForm(args.get(4));
            Semester sem = Semester.getSem(args.get(5));
            float coordinatesX = Float.parseFloat(args.get(6));
            double coordinatesY = Double.parseDouble(args.get(7));
            Float height = Float.parseFloat(args.get(9));
            double weight = Double.parseDouble(args.get(10));
            Color color = Color.getColor(args.get(11));
            Long x = Long.parseLong(args.get(12));
            long y = Long.parseLong(args.get(14));
            int z = Integer.parseInt(args.get(15));
            Location loc = new Location(x, y, z, args.get(13));
            Person admin = new Person(args.get(8), height, weight, color, loc);
            Coordinates coord = new Coordinates(coordinatesX, coordinatesY);
            StudyGroup el = new StudyGroup(args.get(1), studentsCount, shouldBeExpelled, coord, form, sem, admin);
            StorageOfManagers.storage.putWithKey(key, el);
        }catch(NumberFormatException | IllegalValueException e){
            RequestsManager.manager.answerForming("Значения команды insert  в скрипте не валидны");
        }
        finally{
            commandList.add("insert");
        }
    }
    public void updateFromScript(ArrayList<String> args){
        try{
            int id = Integer.parseInt(args.get(0));
            long studentsCount = Long.parseLong(args.get(2));
            long shouldBeExpelled = Long.parseLong(args.get(3));
            FormOfEducation form = FormOfEducation.getForm(args.get(4));
            Semester sem = Semester.getSem(args.get(5));
            float coordinatesX = Float.parseFloat(args.get(6));
            double coordinatesY = Double.parseDouble(args.get(7));
            Float height = Float.parseFloat(args.get(9));
            double weight = Double.parseDouble(args.get(10));
            Color color = Color.getColor(args.get(11));
            Long x = Long.parseLong(args.get(12));
            long y = Long.parseLong(args.get(14));
            int z = Integer.parseInt(args.get(15));
            Location loc = new Location(x, y, z, args.get(13));
            Person admin = new Person(args.get(8), height, weight, color, loc);
            Coordinates coord = new Coordinates(coordinatesX, coordinatesY);
            StudyGroup el = new StudyGroup(args.get(1), studentsCount, shouldBeExpelled, coord, form, sem, admin);
            el.setId(id);
            RequestsManager.manager.answerForming(StorageOfManagers.storage.replaceElement(id, el));
        }catch(NumberFormatException | IllegalValueException e){
            RequestsManager.manager.answerForming("Значения команды update в скрипте не валидны");
        }
        finally{
            commandList.add("update");
        }
    }

    public void commandExecute(String s) throws org.example.island.details.exceptions.NoSuchCommandException {
        String[] str = parseCommand(s);
        Command command = getCommand(str[0].toLowerCase());
        commandList.add(str[0]);
        String[] args = Arrays.copyOfRange(str, 1, str.length);
        command.setArguments(args);
        command.execute(this);
    }

    public String[] parseCommand(String s){
        return s.split(" ");
    }


    public void addCommand(Command cmd){
        commandRegistry.put(cmd.getName(), cmd);
    }

    public Command getCommand(String s) throws NoSuchCommandException {
        if(!commandRegistry.containsKey(s)){
            RequestsManager.manager.answerForming("Команда не найдена, повторите ввод");
        }
        return commandRegistry.getOrDefault(s, null);
    }

}
