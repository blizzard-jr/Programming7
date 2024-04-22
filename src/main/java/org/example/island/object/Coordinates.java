package org.example.island.object;


import org.example.InputProcess;
import org.example.exceptions.IllegalValueException;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private float x; //Значение поля должно быть больше -417
    private double y; //Значение поля должно быть больше -574

    public Coordinates(float x, double y){
        setX(x);
        setY(y);
    }
    public Coordinates(){}
    public float getX(){
        return this.x;
    }
    public void setX(float x){
        if(!new InputProcess().validate(x, -417)){
            throw new IllegalValueException("Значение координаты x должно быть больше -417");
        }
        else{
            this.x = x;
        }
    }

    public double getY(){
        return this.y;
    }
    public void setY(double y){
        if(!new InputProcess().validate(y, -574)){
            throw new IllegalValueException("Значение координату y должно быть больше -574");
        }
        else{
            this.y = y;
        }

    }
    @Override
    public String toString(){
        return "x: " + this.x + ", y: " + this.y;
    }
}
