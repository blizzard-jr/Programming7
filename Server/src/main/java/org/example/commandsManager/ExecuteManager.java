package org.example.commandsManager;

import org.example.answers.AnswerManager;
import org.example.details.Storage;
import org.example.exceptions.IllegalValueException;
import org.example.fileSystem.FileSystem;
import org.example.island.commands.Command;
import org.example.island.commands.Execute_script;
import org.example.island.details.exceptions.NoSuchCommandException;
import org.example.island.object.*;
import org.example.requests.RequestsManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class ExecuteManager {
    FileSystem fileSystem = new FileSystem();
    Storage storage = new Storage();
    public void executeCLear(){
        String answer = storage.clear();
        RequestsManager.manager.answerForming(answer);
    }
    public void executeCountEdu(Object[] args){
        FormOfEducation form = (FormOfEducation) args[0];
        int count = 0;
        for(StudyGroup group : storage.getValues()){
            if(group.getFormOfEducation().compareTo(form) < 0){
                count+=1;
            }
        }
        String answer = "Поле FormOfEducation меньше заданного вами значения у " + count + " элементов";
        RequestsManager.manager.answerForming(answer);
    }

    public void executeScript(Object[] args){
        FileInputStream stream = null;
        try {
            stream = new FileInputStream((String) args[0]);
        } catch (FileNotFoundException e) {
            RequestsManager.manager.answerForming(e.getMessage());
            return;
        }
        if(!Execute_script.files.contains((String) args[0])){
            Execute_script.files.add((String) args[0]);
            fileSystem.parseScript(stream);
            System.out.println("Выполнение скрипта завершено");
        }
        else{
            RequestsManager.manager.answerForming("Не не, слишком бесконечно получается");
        }
    }
    public void executeFilterStudentsCount(Object[] args){
        long studentCount = 0;
        try{
            studentCount = Long.parseLong((String) args[0]);
        }catch(NumberFormatException e){
            RequestsManager.manager.answerForming(e.getMessage());
        }
        int flag = 0;
        List<String> list = new ArrayList<>();
        for(StudyGroup el : storage.getValues()){
            if(el.getStudentsCount() == studentCount){
                flag+=1;
                list.add(el.toString());
            }
        }
        if(flag == 0){
            RequestsManager.manager.answerForming("В ходе выполнения программы совпадений не выявлено");
        }
        else{
            RequestsManager.manager.answerForming((String[]) list.toArray());
        }
    }
    public void executeFilterEdu(Object[] args){
        FormOfEducation form = (FormOfEducation) args[0];
        boolean flag = false;
        List<String> list = new ArrayList<>();
        for(StudyGroup group : storage.getValues()){
            if(group.getFormOfEducation().compareTo(form) < 0){
                flag = true;
                list.add(group.toString());
            }
        }
        if(!flag){
            RequestsManager.manager.answerForming("Элементов, у которых значение поля FormOfEducation меньше заданного вами значения не нашлось");
        }
        else{
            RequestsManager.manager.answerForming((String[]) list.toArray());
        }
    }
    public void executeInfo(){
        RequestsManager.manager.answerForming(storage.toString());
    }
    public void executeInsert(Object[] args){
        StudyGroup obj = (StudyGroup) args[1];
        Integer key = Integer.parseInt((String) args[0]);
        storage.putWithKey(key, obj);
        storage.putMapKeys(key, obj.getId());
        RequestsManager.manager.answerForming("Объект успешно добавлен");
    }

    public void executeRemoveGreater(Object[] args){
        StudyGroup el = (StudyGroup) args[0];
        ArrayList<StudyGroup> keys = new ArrayList<>();
        for(StudyGroup element : storage.getValues()){
            if(el.compareTo(element) > 0){
                keys.add(element);
            }
        }
        int count = 0;
        for(StudyGroup groups : keys){
            if(storage.removeElement(storage.findKey(groups.getId()))){
                count+=1;
            }
        }
        RequestsManager.manager.answerForming("В ходе исполнения команды было удалено " + count + " объектов");

    }

    public void executeRemove(Object[] args){
        int key = 0;
        try{
            key = Integer.parseInt((String) args[0]);
        }catch (NumberFormatException e){
            RequestsManager.manager.answerForming(e.getMessage());
        }
        if(storage.removeElement(key)){
            RequestsManager.manager.answerForming("Объект успешно удалён");
        }
        else{
            RequestsManager.manager.answerForming("Объект, соответсвующий введённому вами ключу в коллекции не найден");
        }
    }
    public void executeRemoveLower(Object[] args){
        int key = 0;
        try{
            key = Integer.parseInt((String) args[0]);
        }catch(NumberFormatException e){
            RequestsManager.manager.answerForming(e.getMessage());
        }
        ArrayList<Integer> keys = new ArrayList<>();
        for(Integer key_i : storage.getKeys()){
            if(key > key_i){
                keys.add(key_i);
            }
        }
        if(keys.size() == storage.getSize()){
            RequestsManager.manager.answerForming("Введённый вами ключ меньше всех ключей, что есть в коллекции, удаление элементов не было произведено");
        }
        else{
            int count = 0;
            for (Integer integer : keys) {
                if(storage.removeElement(integer)){
                    count += 1;
                }
            }
            RequestsManager.manager.answerForming("В ходе исполнения команды было удалено " + count + " объектов");
        }
    }
    public void executeSave(){
        fileSystem.parseToFile(storage.getMap());
    }

    public void executeShow(){
        List<String> list = new ArrayList<>();
        Collection<StudyGroup> s = storage.getValue();
        for (StudyGroup group : s) {
            list.add(group.toString());
        }
        RequestsManager.manager.answerForming((String[]) list.toArray());
    }
    public void executeUpdate(Object[] args){
        StudyGroup element = (StudyGroup) args[1];
        Long id = (Long) args[0];
        element.setId(id);
        storage.replaceElement(id, element);
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
            storage.putWithKey(key, el);
        }catch(NumberFormatException | IllegalValueException e){
            throw new IllegalValueException("Значения команды insert  в скрипте не валидны");
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
            el.setId((long) id);
            storage.replaceElement(id, el);
        }catch(NumberFormatException | IllegalValueException e){
            throw new IllegalValueException("Значения команды update в скрипте не валидны");
        }
    }
    /*
    public void executeCommand(String s) throws NoSuchCommandException {
        String[] str = parseCommand(s);
        Command command = getCommand(str[0].toLowerCase());
        commandList.add(str[0]);
        String[] args = Arrays.copyOfRange(str, 1, str.length);
        command.execute(args);
    }

     */

}
