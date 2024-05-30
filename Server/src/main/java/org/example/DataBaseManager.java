package org.example;

import org.example.details.StorageOfManagers;
import org.example.island.object.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class DataBaseManager {
    private Connection connection;
    private String U_login;
    public DataBaseManager(Connection connection){
        this.connection = connection;
    }
    public void collectionInit(){
        try {
            StorageOfManagers.storage.clear();
            Location location = null;
            Person person = null;
            Coordinates coordinates = null;
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM StudyGroup");
            while(result.next()){
                Long x = result.getLong("L_x");
                long y = result.getLong("L_y");
                int z = result.getInt("L_z");
                String name = result.getString("L_name");
                location = new Location(x, y, z, name);
                String P_name = result.getString("P_name");
                Float height = result.getFloat("P_height");
                double weight = result.getDouble("P_weight");
                Color color = Color.getColor(result.getString("P_color"));
                person = new Person(P_name, height, weight, color, location);
                float coordinatesX = result.getFloat("C_x");
                double coordinatesY = result.getDouble("C_y");
                coordinates = new Coordinates(coordinatesX, coordinatesY);
                int key = result.getInt("key");
                String G_name = result.getString("S_name");
                long studentsCount = result.getLong("studentsCount");
                long shouldBeExpelled = result.getLong("shouldBeExpelled");
                FormOfEducation form = FormOfEducation.getForm(result.getString("formOfEducation"));
                Semester sem = Semester.getSem(result.getString("semester"));
                StudyGroup el = new StudyGroup(G_name, studentsCount, shouldBeExpelled, coordinates, form, sem, person);
                StorageOfManagers.storage.putWithKey(key, el);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean authorisation(String login, String password){
        try {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery("SELECT login FROM UsersData");
            boolean flag = false;
            while(res.next()){
                String log = res.getString("login");
                if(login.equals(log)){
                    flag =  true;
                }
            }
            if(!flag){
                return false;
            }
            flag = false;
            PreparedStatement ps = connection.prepareStatement("SELECT salt FROM UsersData WHERE login = ?");
            ps.setString(1, login);
            res = ps.executeQuery();
            String salt = null;
            if(res.next()){
                salt = res.getString("salt");
            }
            byte[] pass = cached(password, salt);
            res = statement.executeQuery("SELECT password FROM UsersData");
            while(res.next()){
                if(res.getString("password").equals(Arrays.toString(pass))){
                    flag = true;
                }
            }
            U_login = login;
            return flag;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean registration(String login, String password){
        try {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery("SELECT login FROM UsersData");
            boolean flag = true;
            while(res.next()){
                String log = res.getString("login");
                if(login.equals(log)){
                    flag =  false;
                }
            }
            if(!flag){
                return false;
            }
            PreparedStatement ps = connection.prepareStatement("INSERT INTO UsersData VALUES(?, ?, ?)");
            String salt = getSalt();
            byte[] pass = cached(password, salt);
            ps.setString(1, login);
            ps.setString(2, Arrays.toString(pass));
            ps.setString(3, salt);
            ps.executeUpdate();
            U_login = login;
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] cached(String password, String salt){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            return md.digest((password + salt).getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public String getSalt(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
    public boolean executeCLear(){
        try {
            PreparedStatement groupSt = connection.prepareStatement("DELETE FROM Studygroup WHERE owner = ?");
            groupSt.setString(1, U_login);
            StorageOfManagers.storage.clear();
            return groupSt.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean executeInsert(ArrayList<Object> args){
        Integer key = (Integer) args.get(0);
        StudyGroup obj = (StudyGroup) args.get(1);
        Person person = obj.getGroupAdmin();
        Location location = person.getLocation();
        Coordinates coordinates = obj.getCoordinates();
        try {
            PreparedStatement statement = connection.prepareStatement("insert into StudyGroup values(default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, default, ?)");
            statement.setInt(1,key);
            statement.setString(2, obj.getName());
            statement.setLong(3, obj.getStudentsCount());
            statement.setLong(4, obj.getShouldBeExpelled());
            statement.setString(5, obj.getFormOfEducation().getRus());
            statement.setString(6, obj.getSemesterEnum().getRus());
            statement.setFloat(7, coordinates.getX());
            statement.setDouble(8, coordinates.getY());
            statement.setString(9, person.getName());
            statement.setFloat(10, person.getHeight());
            statement.setDouble(11, person.getWeight());
            statement.setString(12, person.getHairColor().getRus());
            statement.setLong(13, location.getX());
            statement.setLong(14, location.getY());
            statement.setLong(15, location.getZ());
            statement.setString(16, location.getName());
            statement.setString(17, U_login);
            statement.executeUpdate();
            collectionInit();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*
        StorageOfManagers.storage.putWithKey(key, obj);
        StorageOfManagers.storage.putMapKeys(key, obj.getId());
        msg.setArguments("Объект успешно добавлен");

         */
    }

    public int executeRemoveGreater(ArrayList<Object> args){
        StudyGroup el = (StudyGroup) args.get(0);
        try {
            int count = 0;
            PreparedStatement groupSt = connection.prepareStatement("DELETE FROM Studygroup WHERE owner = ? AND id = ?");
            groupSt.setString(1, U_login);
            for(StudyGroup obj : StorageOfManagers.storage.getValues()){
                if(el.compareTo(obj) > 0){
                    groupSt.setLong(2, obj.getId());
                    groupSt.executeUpdate();
                    count ++;
                }
            }
            if(count > 0){
                collectionInit();
            }
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean executeRemove(ArrayList<Object> args){
        int key = 0;
        try{
            key = (Integer) args.get(0);
            PreparedStatement groupSt = connection.prepareStatement("DELETE FROM Studygroup WHERE owner = ? AND key = ?");
            groupSt.setString(1, U_login);
            groupSt.setInt(2, key);
            groupSt.executeUpdate();
        }catch (NumberFormatException | SQLException e){
            return false;
        }
        collectionInit();
        return true;
    }
    public Integer executeRemoveLower(ArrayList<Object> args){
        Set<Integer> keys;
        int key;
        try{
            int count = 0;
            PreparedStatement groupSt = connection.prepareStatement("DELETE FROM Studygroup WHERE owner = ? AND key = ?");
            groupSt.setString(1, U_login);
            key = Integer.parseInt((String) args.get(0));
            keys = StorageOfManagers.storage.getKeys();
            for(int k : keys){
                if(k < key){
                    groupSt.setInt(2, k);
                    groupSt.executeUpdate();
                    count ++;
                }
            }
            if(count > 0){
                collectionInit();
            }
            return count;
        }catch(NumberFormatException | SQLException e){
            return null;
        }
    }
    public boolean executeUpdate(ArrayList<Object> args){
        StudyGroup element = (StudyGroup) args.get(1);
        Integer id = (Integer) args.get(0);
        PreparedStatement groupSt = null;
        try {
            groupSt = connection.prepareStatement("UPDATE StudyGroup SET s_name = ?, studentscount = ?, shouldbeexpelled = ?, formOfEducation = ?, semester = ?, c_x = ?, c_y = ?, p_name = ?, p_weight = ?, p_height = ?, p_color = ?, l_x = ?, l_y = ?, l_z = ?, l_name = ? where owner = ?");
            groupSt.setString(1, element.getName());
            groupSt.setLong(2, element.getStudentsCount());
            groupSt.setLong(3, element.getShouldBeExpelled());
            groupSt.setString(4, element.getFormOfEducation().getRus());
            groupSt.setString(5, element.getSemesterEnum().getRus());
            groupSt.setFloat(6, element.getCoordinates().getX());
            groupSt.setDouble(7, element.getCoordinates().getY());
            groupSt.setString(8, element.getGroupAdmin().getName());
            groupSt.setDouble(9, element.getGroupAdmin().getWeight());
            groupSt.setFloat(10, element.getGroupAdmin().getHeight());
            groupSt.setString(11, element.getGroupAdmin().getHairColor().getRus());
            groupSt.setLong(12, element.getGroupAdmin().getLocation().getX());
            groupSt.setLong(13, element.getGroupAdmin().getLocation().getY());
            groupSt.setLong(14, element.getGroupAdmin().getLocation().getZ());
            groupSt.setString(15, element.getGroupAdmin().getLocation().getName());
            groupSt.setString(16, U_login);
            groupSt.executeUpdate();
            collectionInit();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


}
