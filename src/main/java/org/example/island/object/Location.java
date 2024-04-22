package org.example.island.object;


import org.example.InputProcess;
import org.example.exceptions.IllegalValueException;

import java.io.Serializable;

public class Location implements Serializable {

    private Long x; //Поле не может быть null
    private long y;
    private int z;
    private String name; //Строка не может быть пустой, Поле не может быть null
    public Location(Long x, long y, int z, String name){
        setName(name);
        setX(x);
        setY(y);
        setZ(z);
    }
    public Location(){}
    public void setX(Long x) {
        if(!new InputProcess().validate(x,false)){
            throw new IllegalValueException("Значение не может быть null");
        }
        else{
            this.x = x;
        }

    }

    public void setY(long y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(!new InputProcess().validate(name, false)){
            throw new IllegalValueException("Значение не может быть null или пустой строкой");
        }
        else{
            this.name = name;
        }

    }

    @Override
    public String toString(){
        return "x: " + this.x + ", y: " + this.y + ", z: " + this.z + ", name: " + this.name;
    }
}
