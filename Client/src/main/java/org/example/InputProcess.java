package org.example;



import org.example.exceptions.IllegalValueException;
import org.example.island.object.*;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputProcess  {
    private Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    /**
     * Метод читает ввод пользователя с предварительным выводом сообщения, внутри используется валидация для строк
     * @param message
     * @return String
     */
    public String readWithMessage(String message){
        System.out.println(message);
        String s = scanner.nextLine();
        while(true){
            if(s.isEmpty()){
                writeErr("Поле не может быть пустым, повторите ввод");
                s = scanner.nextLine();
            }
            else{
                break;
            }
        }
        return s;
    }
    /**
     * Метод читает ввод пользователя с предварительным выводом сообщения, внутри используется валидация для числовых значений
     * @param message
     * @return String
     */
    public String readWithMessage(String message, boolean acceptCompare, int compare, boolean acceptFloat) {
        double numb;
        String s = readWithMessage(message);
        Pattern floatPattern = Pattern.compile("[-+]?[0-9]*\\.[0-9]+");
        Matcher matcherFloat = floatPattern.matcher(s);
        if(matcherFloat.find() && acceptFloat){
            while (true) {
                try {
                    numb = Double.parseDouble(s);
                    break;
                } catch (NumberFormatException e) {
                    writeErr("Некорректный ввод, попробуйте ещё разок");
                    s = readWithMessage(message);
                }
            }
        }
        else{
            while (true) {
                try {
                    numb = Long.parseLong(s);
                    break;
                } catch (NumberFormatException e) {
                    writeErr("Некорректный ввод, попробуйте ещё раз");
                    s = readWithMessage(message);
                }
            }
        }

        if(acceptCompare){
            if (!compare(numb, compare)) {
                writeErr("Требования к числовому полю не выполнены повторите ввод");
                s = readWithMessage(message, true, compare, acceptFloat);
            }
        }
        return s;
    }
    public Color colorInit(String message){
//        System.out.println(message);
//        System.out.println("Допустимые значения: ");
//        for(Color color : Color.values()){
//            System.out.println(color.getRus());
//        }
//        String s = scanner.nextLine();
//        while(!Color.findColor(s) || s.isEmpty()){
//            writeErr("Значение константы не распознано, повторите ввод");
//            s = scanner.nextLine();
//        }
        return Color.valueOf(message);
    }

    /**
     * Метод инициализирует enum FormOfEducation - поле элемента коллекции
     * @param message
     * @return FormOfEducation
     */
    public FormOfEducation formInit(String message){
        System.out.println(message);
        System.out.println("Допустимые значения: ");
        for(FormOfEducation form : FormOfEducation.values()){
            System.out.println(form.getRus());
        }
        String s = scanner.nextLine();
        while(!FormOfEducation.findForm(s) || s.isEmpty()){
            writeErr("Значение константы не распознано, повторите ввод");
            s = scanner.nextLine();
        }
        return FormOfEducation.getForm(s);
    }
    public boolean hasNextLine(){
        return scanner.hasNextLine();
    }
    /**
     * Метод инициализирует enum Semester - поле элемента коллекции
     * @param message
     * @return FormOfEducation
     */

    public Semester semInit(String message){
        System.out.println(message);
        System.out.println("Допустимые значения: ");
        for(Semester sem : Semester.values()){
            System.out.println(sem.getRus());
        }
        String s = scanner.nextLine();
        while(!Semester.findSem(s) || s.isEmpty()){
            writeErr("Значение константы не распознано, повторите ввод");
            s = scanner.nextLine();
        }
        return Semester.getSem(s);
    }
    /**
     * Метод инициализирует location - поле элемента коллекции
     * @return FormOfEducation
     */
    public Location locationInit(){
        Long x = Long.parseLong(readWithMessage("Введите координату админа x: ", false, 0, false));
        String name = readWithMessage("Введите названия локации админа: ");
        return new Location(x, Long.parseLong(readWithMessage("Введите координату админа y: ", false, 0, false)), Integer.parseInt(readWithMessage("Введите координату админа z: ", false, 0, false)), name);
    }
    /**
     * Метод инициализирует coordinates - поле элемента коллекции
     * @return FormOfEducation
     */
    public Coordinates coordinatesInit(){
        float x = Float.parseFloat(readWithMessage("Введите координату x: ",true,  -417, true));
        double y = Double.parseDouble(readWithMessage("Введите координату y: ",true,  -574, true));
        return new Coordinates(x, y);
    }
    /**
     * Метод инициализирует groupAdmin - поле элемента коллекции
     * @return FormOfEducation
     */
    public Person personInit(){
        return new Person(readWithMessage("Введите имя админа группы: "),
                Float.parseFloat(readWithMessage("Введите рост админа: ",true,  0, true)),
                Double.parseDouble(readWithMessage("Введите вес админа: ",true,  0, true)),
                colorInit("Введите цвет волос админа: "),
                locationInit());
    }
    /**
     * Метод инициализирует элемент коллекции
     * @return FormOfEducation
     */
    public StudyGroup studyGroupInit(String name, long studentsCount, long shouldBeExpelled){
        FormOfEducation formOfEducation = formInit("Введите значение формы обучения");
        Semester sem = semInit("Введите константу семестра: ");
        return new StudyGroup(name, studentsCount, shouldBeExpelled, coordinatesInit(), formOfEducation, sem, personInit());
    }

    public Object[] objectIdentity(String[] args){
        if(args.length == 4){
            Integer keyOrId;
            String name = args[1];
            long studentsCount;
            long studentsShouldBeExpelled;
            try{
                keyOrId = Integer.parseInt(args[0]);
                studentsCount = Long.parseLong(args[2]);
                studentsShouldBeExpelled = Long.parseLong(args[3]);
            } catch (NumberFormatException e) {
                throw new IllegalValueException("Введены некорректные значения для объекта");
            }
            if(args[1].isEmpty() || studentsCount <=0 || studentsShouldBeExpelled <=0){
                throw new IllegalValueException("Введённые значения не валидны");
            }
            StudyGroup o = studyGroupInit(name, studentsCount, studentsShouldBeExpelled);
            Object[] args1 = new Object[2];
            args1[0] = keyOrId;
            args1[1] = o;
            return args1;
        }
        else if (args.length == 3){
            String name = args[0];
            long studentsCount;
            long studentsShouldBeExpelled;
            try{
                studentsCount = Long.parseLong(args[1]);
                studentsShouldBeExpelled = Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                throw new IllegalValueException("Введены некорректные значения для объекта");
            }
            if(args[0].isEmpty() || studentsCount <=0 || studentsShouldBeExpelled <=0){
                throw new IllegalValueException("Введённые значения не валидны");
            }
            StudyGroup o = studyGroupInit(name, studentsCount, studentsShouldBeExpelled);
            Object[] args1 = new Object[1];
            args1[0] = o;
            return args1;
        }
        else{
            throw new IllegalValueException("Неверное количество аргументов команды");
        }
    }
    public boolean compare(double num, int comp){
        return num > comp;
    }
    public void writeErr(Object obj){
        System.err.println(obj);
    }
    /**
     * Метод проверяет валидность полей элемента коллекции
     * @param obj
     * @param acceptEmpty       Разрешено ли значение пустой строки
     */
    public boolean validate(Object obj, boolean acceptEmpty){
        return !(!acceptEmpty & (obj.equals("") || obj == null));
    }

    /**
     * Метод проверяет валидность числовых полей
     * @param numb
     * @param compare
     */
    public boolean validate(double numb,int compare){
        return numb > compare;
    }

}
