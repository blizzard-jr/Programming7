package org.example.commandsManager;

import org.example.details.StorageOfManagers;
import org.island.commands.*;
import org.island.details.ServiceConst;
import org.island.details.exceptions.IllegalValueException;
import org.island.details.exceptions.NoSuchCommandException;
import org.island.object.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;


public class ExecuteManager {
    private ForkJoinPool answerPool = ForkJoinPool.commonPool();
    private Map<String, Command> commandRegistry = new HashMap<>();
    private Map<Object, String> commandEx = new HashMap<>();
    private ArrayList<String> commandList = new ArrayList<>();
    private Message msg = new Message();



    public ExecuteManager(){
        addCommand("executeShow", new Show());
        addCommand("executeInfo", new Info());
        addCommand("executeInsert", new InsertNull());
        addCommand("executeUpdate", new UpdateId());
        addCommand("executeRemove", new Remove_key());
        addCommand("executeClear", new Clear());
        addCommand("executeScript", new Execute_script());
        addCommand("executeRemoveGreater", new Remove_greater());
        addCommand("executeRemoveLower", new Remove_lower_key());
        addCommand("executeHistory", new History());
        addCommand("executeCountEdu", new Count_less_than_form_of_education());
        addCommand("executeFilterStudentsCount", new Filter_by_students_count());
        addCommand("executeFilterEdu", new Filter_less_than_form_of_education());
        addCommand("executeExit", new Exit());
        addCommand("executeHelp", new Help());
        addCommand("executeMessage", new Message());
    }
    public void commandExecute(Command cmd){
        try {
            Method mth;
            if(cmd.getArgumentCount() > 3 || cmd.getClass() == Clear.class){
                mth = this.getClass().getMethod(commandEx.get(cmd.getClass()), cmd.getArguments().getClass());
                mth.invoke(this, cmd.getArguments());
            }
            else{
                mth = this.getClass().getMethod(commandEx.get(cmd.getClass()));
                mth.invoke(this);
            }
        } catch (NoSuchMethodException  | IllegalAccessException | InvocationTargetException e) {
            msg.setArguments("Во время выполнения команды произошла непредвиденная ошибка");
        }
        ArrayList<Object> args = cmd.getArguments();
        answerPool.invoke(new ResultSending(msg, (Socket) args.get(args.size() - 1)));
        msg.clearArg();
    }
    public void commandExecute(String s, Object login, Object password, Socket clientSocket)  {
        String[] str = parseCommand(s);
        Command command = null;
        try {
            command = getCommand(str[0].toLowerCase());
        } catch (NoSuchCommandException e) {
            msg.setArguments("Сервер не смог распознать команду");
        }
        commandList.add(str[0]);
        String[] args = Arrays.copyOfRange(str, 1, str.length);
        command.setArguments(args);
        command.setArguments(login);
        command.setArguments(password);
        command.setArguments(clientSocket);
        commandExecute(command);
    }

    public Map<String, Command> getCommandRegistry() {
        return commandRegistry;
    }

    public void executeHelp(){
        Map<String, Command> map = getCommandRegistry();
        for(Command command : map.values()){
            msg.setArguments(command.getName() + " - " + command.getDescription());
        }
    }
    public ArrayList<String> getCommandList() {
        return commandList;
    }


    public void executeHistory(){
        ArrayList<String> list = getCommandList();
        if(list.size() < 14){
            msg.setArguments("Количества исполненных команд не достаточно для вывода истории");
        }
        else{
            for (int i = 0; i < 14; i++) {
                msg.setArguments(list.get(i));
            }
        }
    }
    public void executeClear(ArrayList<Object> args){
        try {
            if(StorageOfManagers.dBManager.executeCLear(args) != 0){
                msg.setArguments("ок");
            }
            else{
                msg.setArguments("В коллекции не нашлось объектов, принадлежащих вам");
            }
        } catch (SQLException e) {
            msg.setArguments(e.getMessage());
        }finally {
            try {
                msg.setArguments(StorageOfManagers.dBManager.collectionInit());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void executeCountEdu(ArrayList<Object> args){
        FormOfEducation form = (FormOfEducation) args.get(0);
        long count = StorageOfManagers.storage.getValues().stream().filter(group -> group.getFormOfEducation().compareTo(form) < 0).count();
        String answer = "Поле FormOfEducation меньше заданного вами значения у " + count + " элементов";
        msg.setArguments(answer);
    }

    public void executeScript(ArrayList<Object> args){
        FileInputStream stream = null;
        try {
            stream = new FileInputStream((String) args.get(0));
        } catch (FileNotFoundException e) {
            msg.setArguments("Файл который вы указали не найден");
            return;
        }
        if(!Execute_script.files.contains((String) args.get(0))){
            Execute_script.files.add((String) args.get(0));
            msg.setArguments(StorageOfManagers.fileSystem.parseScript(stream, args.get(1), args.get(2), (Socket) args.get(args.size() - 1)));
            msg.setArguments("Выполнение скрипта " + Execute_script.files.size() + " завершено");
            Execute_script.files.remove((String) args.get(0));
        }
        else{
            msg.setArguments("Не не, слишком бесконечно получается");
        }

    }
    public void executeFilterStudentsCount(ArrayList<Object> args){
        List<String> list = new ArrayList<>();
        long studentCount;
        long count = 0;
        try{
            studentCount = Long.parseLong((String) args.get(0));
            count = StorageOfManagers.storage.getValues().stream().filter(group -> group.getStudentsCount() == studentCount).map(group -> list.add(group.toString())).count();
        }catch(NumberFormatException e){
            msg.setArguments("Не удалось спарсить переданные вами данные");
        }

        if(count == 0){
            msg.setArguments("В ходе выполнения программы совпадений не выявлено");
        }
        else{
            msg.setArguments(list.toArray());
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
        List<String> list = new ArrayList<>();
        long count = StorageOfManagers.storage.getValues().stream().filter(group -> group.getFormOfEducation().compareTo(form) < 0).count();
        if(count == 0){
            msg.setArguments("Элементов, у которых значение поля FormOfEducation меньше заданного вами значения не нашлось");
        }
        else{
            msg.setArguments(list.toArray());
        }
    }
    public void executeInfo(){
        msg.setArguments(StorageOfManagers.storage.toString());
    }
    public void executeInsert(ArrayList<Object> args){
        int count = 0;
        try {
            count = StorageOfManagers.dBManager.executeInsert(args);
        } catch (SQLException e) {
            msg.setArguments(e.getMessage());
        }
        if(count != 0) {
            msg.setArguments("ок");
        }
        try {
            msg.setArguments(StorageOfManagers.dBManager.collectionInit());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeRemoveGreater(ArrayList<Object> args){
        int count = 0;
        try {
            count = StorageOfManagers.dBManager.executeRemoveGreater(args);
        } catch (SQLException e) {
            msg.setArguments(e.getMessage());
        }
        msg.setArguments("В ходе исполнения команды было удалено " + count + " объектов");
    }

    public void executeRemove(ArrayList<Object> args){
        try {
            if(StorageOfManagers.dBManager.executeRemove(args) > 0){
                msg.setArguments("ок");
            }
            else{
                msg.setArguments("Произошла ошибка в ходе выполнения команды, объект не был удалён");
            }
        } catch (SQLException e) {
            msg.setArguments(e.getMessage());
        }finally {
            try {
                msg.setArguments(StorageOfManagers.dBManager.collectionInit());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void executeRemoveLower(ArrayList<Object> args){
        int count = 0;
        try {
            count = StorageOfManagers.dBManager.executeRemoveLower(args);
        } catch (SQLException e) {
            msg.setArguments(e.getMessage());
        }
        if(count == 0){
            msg.setArguments("Введённый вами ключ меньше всех ключей, что есть в коллекции, удаление элементов не было произведено");
        }
        else{
            msg.setArguments("В ходе исполнения команды было удалено " + count + " объектов");
        }
    }
    public void executeExit(){

        msg.setArguments("Всего доброго!");
    }

    public void executeMessage(ArrayList<Object> listArg){
        Object data = listArg.get(0);
        if(data.equals(ServiceConst.AUTHORISATION)){
            boolean success = false;
            try {
                success = StorageOfManagers.dBManager.authorisation((String) listArg.get(1), (String) listArg.get(2));
            } catch (SQLException e) {
                msg.setArguments(e.getMessage());
            }
            if(success){
                msg.setArguments("Авторизация прошла успешно");
                try {
                    msg.setArguments(StorageOfManagers.dBManager.collectionInit());
                } catch (SQLException e) {
                    msg.setArguments(e.getMessage());
                }
            }
            else{
                msg.setArguments("Попытка завершилась неудачей");
            }
        }else if(data.equals(ServiceConst.REGISTRATION)){
            boolean success = false;
            try {
                success = StorageOfManagers.dBManager.registration((String) listArg.get(1), (String) listArg.get(2));
            } catch (SQLException e) {
                msg.setArguments(e.getMessage());
            }
            if(success){
                msg.setArguments("Регистрация прошла успешно");
                try {
                    msg.setArguments(StorageOfManagers.dBManager.collectionInit());
                } catch (SQLException e) {
                    msg.setArguments(e.getMessage());
                }
            }
            else{
                msg.setArguments("Попытка завершилась неудачей");
            }
        }
    }
    public void executeShow(){
        try {
            msg.setArguments("ок");
            msg.setArguments(StorageOfManagers.dBManager.collectionInit());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void executeUpdate(ArrayList<Object> args){
        try {
            if(StorageOfManagers.dBManager.executeUpdate(args) != 0){
                msg.setArguments("ок");
            }
            else {
                msg.setArguments("Объект не удалось заменить");
            }
        } catch (SQLException e) {
            msg.setArguments(e.getMessage());
        }
        finally {
            try {
                msg.setArguments(StorageOfManagers.dBManager.collectionInit());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

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
            Color color = Color.valueOf(args.get(11));
            Long x = Long.parseLong(args.get(12));
            long y = Long.parseLong(args.get(14));
            int z = Integer.parseInt(args.get(15));
            Location loc = new Location(x, y, z, args.get(13));
            Person admin = new Person(args.get(8), height, weight, color, loc);
            Coordinates coord = new Coordinates(coordinatesX, coordinatesY);
            StudyGroup el = new StudyGroup(args.get(1), studentsCount, shouldBeExpelled, coord, form, sem, admin);
            ArrayList<Object> data = new ArrayList<>();
            data.add(key);
            data.add(el);
            data.add(args.get(16));
            StorageOfManagers.dBManager.executeInsert(data);
        }catch(NumberFormatException | IllegalValueException | SQLException e){
            msg.setArguments("Выполнение команды insert завершилось ошибкой: " + e.getMessage());
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
            Color color = Color.valueOf(args.get(11));
            Long x = Long.parseLong(args.get(12));
            long y = Long.parseLong(args.get(14));
            int z = Integer.parseInt(args.get(15));
            Location loc = new Location(x, y, z, args.get(13));
            Person admin = new Person(args.get(8), height, weight, color, loc);
            Coordinates coord = new Coordinates(coordinatesX, coordinatesY);
            StudyGroup el = new StudyGroup(args.get(1), studentsCount, shouldBeExpelled, coord, form, sem, admin);
            ArrayList<Object> data = new ArrayList<>();
            data.add(id);
            data.add(el);
            data.add(args.get(16));
            StorageOfManagers.dBManager.executeUpdate(data);
        }catch(NumberFormatException | IllegalValueException | SQLException e){
            msg.setArguments("Выполнение команды update завершилось ошибкой: " + e.getMessage());
        }
        finally{
            commandList.add("update");
        }
    }
    public void removeGreaterFromScript(ArrayList<String> args){
        long studentsCount = Long.parseLong(args.get(1));
        long shouldBeExpelled = Long.parseLong(args.get(2));
        FormOfEducation form = FormOfEducation.getForm(args.get(3));
        Semester sem = Semester.getSem(args.get(4));
        float coordinatesX = Float.parseFloat(args.get(5));
        double coordinatesY = Double.parseDouble(args.get(6));
        Float height = Float.parseFloat(args.get(8));
        double weight = Double.parseDouble(args.get(9));
        Color color = Color.valueOf(args.get(10));
        Long x = Long.parseLong(args.get(11));
        long y = Long.parseLong(args.get(13));
        int z = Integer.parseInt(args.get(14));
        Location loc = new Location(x, y, z, args.get(12));
        Person admin = new Person(args.get(7), height, weight, color, loc);
        Coordinates coord = new Coordinates(coordinatesX, coordinatesY);
        StudyGroup el = new StudyGroup(args.get(0), studentsCount, shouldBeExpelled, coord, form, sem, admin);
        ArrayList<Object> data = new ArrayList<>();
        data.add(el);
        data.add(args.get(15));
    }



    public String[] parseCommand(String s){
        return s.split(" ");
    }


    public void addCommand(String method, Command cmd){
        commandEx.put(cmd.getClass(), method);
        commandRegistry.put(cmd.getName(), cmd);
    }

    public Command getCommand(String s) throws NoSuchCommandException {
        if(!commandRegistry.containsKey(s)){
            msg.setArguments("Команда не найдена, повторите ввод");
        }
        return commandRegistry.getOrDefault(s, null);
    }

}
