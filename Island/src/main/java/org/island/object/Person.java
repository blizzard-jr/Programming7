package org.island.object;



import org.island.details.exceptions.IllegalValueException;

import java.io.Serializable;

public class Person implements Serializable {

    private String name; //Поле не может быть null, Строка не может быть пустой
    private Float height; //Поле не может быть null, Значение поля должно быть больше 0
    private double weight; //Значение поля должно быть больше 0
    private Color hairColor; //Поле не может быть null
    private Location location; //Поле не может быть null
    public Person(String name, Float height, double weight, Color hairColor, Location location){
        setName(name);
        setHeight(height);
        setWeight(weight);
        setHairColor(hairColor);
        setLocation(location);
    }
    public Person(){}
    public void setName(String name) {
        if(name == null || name.isEmpty() ){
           throw new IllegalValueException("Значение не может быть null или пустой строкой");
        }
        else{
            this.name = name;
        }
    }

    public void setHeight(float height) {
        if (height > 0){
            this.height = height;
        }
        else{
            throw new IllegalValueException("Значение поля должно быть больше нуля и не null");
        }
    }

    public void setWeight(double weight) {
        if(weight > 0){
            this.weight = weight;
        }
        else{
            throw new IllegalValueException("Значение поля должно быть больше 0");
        }
    }

    public void setHairColor(Color hairColor) {
        if(hairColor == null){
            throw new IllegalValueException("Значение не может быть null");
        }
        else{
            this.hairColor = hairColor;
        }
    }

    public String getName() {
        return name;
    }

    public Float getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if(location == null){
            throw new IllegalValueException("Значение не может быть null");
        }
        else{
            this.location = location;
        }

    }
    @Override
    public String toString(){
        return "Name: " + this.name + ", Height: " + this.height + ", Weight: " + this.weight + ", HairColor: " + this.hairColor + ", Location: " + location.toString();
    }
}
