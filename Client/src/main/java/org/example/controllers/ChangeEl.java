package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.island.commands.UpdateId;
import org.island.object.*;

public class ChangeEl {
    //Добавить логику управления изменения и добавления объекта
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
    private Button apply;
    public MainScene mainScene;
    private int id;
    private String owner;
    private String creationDate;


    public void init(TableGroup gr, MainScene mainScene){
        System.out.println(gr);
        System.out.println(gr.toString());
        F_p_name.setText(gr.getP_name());
        F_gr_name.setText(gr.getGr_name());
        F_p_height.setText(String.valueOf(gr.getP_height()));
        F_st_count.setText(String.valueOf(gr.getSt_count()));
        F_p_weight.setText(String.valueOf(gr.getP_weight()));
        F_p_color.setValue(gr.getP_color());
        F_shouldExp.setText(String.valueOf(gr.getShouldExp()));
        F_form.setValue(gr.getForm());
        F_sem.setValue(gr.getSem());
        F_l_x.setText(String.valueOf(gr.getL_x()));
        F_l_y.setText(String.valueOf(gr.getL_y()));
        F_c_x.setText(String.valueOf(gr.getC_x()));
        F_l_z.setText(String.valueOf(gr.getL_z()));
        F_c_y.setText(String.valueOf(gr.getC_y()));
        F_l_name.setText(gr.getL_name());
        this.mainScene = mainScene;
        this.id = gr.getId();
        this.owner = gr.getOwner();
        this.creationDate = gr.getCreationDate();
    }

    public void insertEl(){

    }
    public void apply(){
        Coordinates coordinates = new Coordinates(
                Float.parseFloat(F_c_x.getText()),
                Double.parseDouble(F_c_y.getText()));
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
        args[0] = id;
        args[1] = studyGroup;
        UpdateId updateId = new UpdateId();
        updateId.setArguments(args);
        mainScene.process(updateId);
        Stage st = (Stage) apply.getScene().getWindow();
        st.close();
    }
    public void updateEl(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GroupFields.fxml"));
    }
    public void deleteEl(){

    }
}
