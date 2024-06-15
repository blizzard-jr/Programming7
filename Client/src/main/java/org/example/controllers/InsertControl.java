package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.island.commands.InsertNull;
import org.example.island.object.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class InsertControl {
    @FXML
    private TextField F_key;
    @FXML
    private TextField F_p_name;
    @FXML
    private TextField F_gr_name;
    @FXML
    private TextField F_p_height;
    @FXML
    private TextField F_st_count;
    @FXML
    private TextField F_p_weight;
    @FXML
    private ChoiceBox F_p_color;
    @FXML
    private TextField F_shouldExp;
    @FXML
    private ChoiceBox F_form;
    @FXML
    private ChoiceBox F_sem;
    @FXML
    private TextField F_l_x;
    @FXML
    private TextField F_l_y;
    @FXML
    private TextField F_c_x;
    @FXML
    private TextField F_l_z;
    @FXML
    private TextField F_c_y;
    @FXML
    private TextField F_l_name;
    @FXML
    private Button insert;
    @FXML
    private Text error_message;
    private MainScene mainScene;

    public void init(MainScene mainScene){
        this.mainScene = mainScene;
        F_form.getItems().addAll(FormOfEducation.values());
        F_p_color.getItems().addAll(Color.values());
        F_sem.getItems().addAll(Semester.values());

    }
    public void insert(){
        AtomicBoolean ok = new AtomicBoolean(false);
        try {
            Coordinates coordinates = new Coordinates(Float.parseFloat(F_c_x.getText()), Double.parseDouble(F_c_y.getText()));
            Location location = new Location(
                    Long.parseLong(F_l_x.getText()),
                    Long.parseLong(F_l_y.getText()),
                    Integer.parseInt(F_l_z.getText()),
                    F_l_name.getText());
            Person person = new Person(
                    F_p_name.getText(),
                    Float.parseFloat(F_p_height.getText()),
                    Double.parseDouble(F_p_weight.getText()),
                    Color.valueOf(F_p_color.getValue().toString()),
                    location);
            StudyGroup studyGroup = new StudyGroup(
                    F_gr_name.getText(),
                    Long.parseLong(F_st_count.getText()),
                    Long.parseLong(F_shouldExp.getText()),
                    coordinates,
                    FormOfEducation.valueOf(F_form.getValue().toString()),
                    Semester.valueOf(F_sem.getValue().toString()),
                    person);
            Object[] args = new Object[2];
            args[0] = Integer.parseInt(F_key.getText());
            args[1] = studyGroup;
            InsertNull insertNull = new InsertNull();
            insertNull.setArguments(args);
            mainScene.process(insertNull);

            ok.set(true);

        } catch(Exception e){
            error_message.setText("введены невалидные данные( повторите попытку");
            Stage st = (Stage) insert.getScene().getWindow();
            st.showAndWait();
        }
        Stage st = (Stage) insert.getScene().getWindow();
        st.close();
    }

}
