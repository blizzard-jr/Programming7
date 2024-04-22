package org.example.island.object;


import org.example.exceptions.IllegalValueException;

import java.io.Serializable;

public enum FormOfEducation implements Serializable {
    DISTANCE_EDUCATION("Дистанционное"),
    FULL_TIME_EDUCATION("Очное"),
    EVENING_CLASSES("Вечернее");
    final String rus;
    FormOfEducation(String rus){
        this.rus = rus;
    }
    public String getRus(){
        return this.rus;
    }
    public static boolean findForm(String s){
        for(FormOfEducation value : FormOfEducation.values()){
            if(value.getRus().equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }
    public static FormOfEducation getForm(String s){
        for(FormOfEducation value : FormOfEducation.values()){
            if(value.getRus().equalsIgnoreCase(s)){
                return value;
            }
        }
        throw new IllegalValueException("значение константы не распознано");// Проверить надобность
    }




}
