package org.example;

import org.example.details.StorageOfManagers;
import org.example.island.object.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {
    private Connection connection;
    public DataBaseManager(Connection connection){
        this.connection = connection;
    }
    public void collectionInit(){
        try {
            Location location = null;
            Person person = null;
            Coordinates coordinates = null;
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM Location");//Не мудри с запросом, разбей на более мелкие, сначала инициализируй локацию, потом админа, и так собирай группу
            while(result.next()){
                Long x = result.getLong("L_x");
                long y = result.getLong("L_y");
                int z = result.getInt("L_z");
                String name = result.getString("L_name");
                location = new Location(x, y, z, name);
            }
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT * FROM Person");
            while(result.next()){
                String name = result.getString("P_name");
                Float height = result.getFloat("P_height");
                double weight = result.getDouble("P_weight");
                Color color = Color.getColor(result.getString("P_color"));
                person = new Person(name, height, weight, color, location);
            }
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT * FROM Coordinates");
            while(result.next()){
                float coordinatesX = result.getFloat("C_x");
                double coordinatesY = result.getDouble("C_y");
                coordinates = new Coordinates(coordinatesX, coordinatesY);
            }
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT * FROM StudyGroup");
            while(result.next()){
                int key = result.getInt("key");
                String name = result.getString("S_name");
                long studentsCount = result.getLong("studentsCount");
                long shouldBeExpelled = result.getLong("shouldBeExpelled");
                FormOfEducation form = FormOfEducation.getForm(result.getString("formOfEducation"));
                Semester sem = Semester.getSem(result.getString("semester"));
                StudyGroup el = new StudyGroup(name, studentsCount, shouldBeExpelled, coordinates, form, sem, person);
                StorageOfManagers.storage.putWithKey(key, el);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
