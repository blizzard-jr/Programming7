package org.example.island.object;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.example.exceptions.IllegalValueException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * Класс - объект коллекции
 */
public class StudyGroup implements Comparable<StudyGroup>, Serializable {
    private Set<Integer> idSet = new HashSet<>();
    private String name; //Поле не может быть null, Строка не может быть пустой
    private long studentsCount; //Значение поля должно быть больше 0
    private long shouldBeExpelled; //Значение поля должно быть больше 0
    private Coordinates coordinates; //Поле не может быть null
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy-HH:mm:ss")
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private FormOfEducation formOfEducation; //Поле не может быть null
    private Semester semesterEnum; //Поле не может быть null
    private Person groupAdmin; //Поле может быть null

    @JsonCreator
    public StudyGroup(@JsonProperty("name") String name, @JsonProperty("studentsCount") long studentsCount, @JsonProperty("shouldBeExpelled")long shouldBeExpelled, @JsonProperty("coordinates")Coordinates coordinates, @JsonProperty("formOfEducation")FormOfEducation formOfEducation, @JsonProperty("semesterEnum")Semester semesterEnum, @JsonProperty("groupAdmin")Person groupAdmin) {
        setName(name);
        setStudentsCount(studentsCount);
        setShouldBeExpelled(shouldBeExpelled);
        setCoordinates(coordinates);
        this.creationDate = LocalDateTime.now();
        this.id = idInit();
        setFormOfEducation(formOfEducation);
        setGroupAdmin(groupAdmin);
        setSemesterEnum(semesterEnum);
    }
    public StudyGroup(){
        this.id = idInit();
        this.creationDate = LocalDateTime.now();
    }


    public void setName(String name) {
        if(!name.isEmpty() & name != null){
            this.name = name;
        }
        else{
            throw new IllegalValueException("Поле name не может быть пустым");
        }
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void setShouldBeExpelled(long shouldBeExpelled) {
        if(shouldBeExpelled > 0){
            this.shouldBeExpelled= shouldBeExpelled;
        }
        else{
            throw new IllegalValueException("Число студентов на отчислении должно быть больше нуля");
        }
    }


    public void setStudentsCount(long studentsCount) {
        if(studentsCount > 0){
            this.studentsCount = studentsCount;
        }
        else{
            throw new IllegalValueException("Число студентов должно быть больше нуля");
        }
    }
    public void setCoordinates(Coordinates coordinates){
        if(coordinates != null){
            this.coordinates = coordinates;
        }
        else{
            throw new IllegalValueException("Объект: координаты не может быть null");
        }
    }

    public void setGroupAdmin(Person groupAdmin) {
        if(groupAdmin != null){
            this.groupAdmin = groupAdmin;
        }
        else{
            throw new IllegalValueException("Объект: groupAdmin не может быть null");
        }

    }
    public void setCreationDate(LocalDateTime creationDate) {
        if(creationDate != null){
            this.creationDate = creationDate;
        }
        else{
            throw new IllegalValueException("Поле CreationDate не может быть равным нулю");
        }
    }
    public void setSemesterEnum(Semester semesterEnum) {
        if(semesterEnum != null){
            this.semesterEnum = semesterEnum;
        }
        else{
            throw new IllegalValueException("Объект: semesterEnum не может быть null");
        }
    }

    public void setFormOfEducation(FormOfEducation formOfEducation) {
        if(formOfEducation != null){
            this.formOfEducation = formOfEducation;
        }
        else{
            throw new IllegalValueException("Объект: formOfEducation не может быть null");
        }
    }
    public long getId(){
        return this.id;
    }

    public String getName() {
        return name;
    }

    public long getStudentsCount() {
        return studentsCount;
    }

    public long getShouldBeExpelled() {
        return shouldBeExpelled;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }
    public int idInit(){
        int id = (int) (Math.random() * 1000);
        while(true){
            if(idSet.contains(id)){
                id += (long) (Math.random() * 10);
            }
            else{
                idSet.add(id);
                break;
            }
        }
        return id;
    }

    @Override
    public String toString(){
        return "Name: " + this.name + ", StudentsCount: " + this.studentsCount + ", ShouldBeExpelled: " + this.shouldBeExpelled + ", Coordinates: " + this.coordinates.toString() + ", creationDate: " + this.creationDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss")) + ", id: " + this.id + ", formOfEducation: " + this.formOfEducation + ", semesterEnum: " + this.semesterEnum + ", groupAdmin: " + this.groupAdmin.toString();
    }
    @Override
    public int compareTo(StudyGroup group){
        return this.name.compareTo(group.name);
    }
    @Override
    public int hashCode(){
        return Objects.hash(this.name, this.creationDate, this.id);
    }


}
