package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import org.example.island.object.StudyGroup;
import java.util.List;


public class MainScene {
    @FXML
    private TableView<StudyGroup> table;
    @FXML
    private TableColumn<StudyGroup, Integer> id;
    @FXML
    private TableColumn<StudyGroup, Integer> key;
    @FXML
    private TableColumn<StudyGroup, String> gr_name;
    @FXML
    private TableColumn<StudyGroup, Integer> st_count;
    @FXML
    private TableColumn<StudyGroup, Integer> shouldExp;
    @FXML
    private TableColumn<StudyGroup, String> form;
    @FXML
    private TableColumn<StudyGroup, String> sem;
    @FXML
    private TableColumn<StudyGroup, Integer> c_x;
    @FXML
    private TableColumn<StudyGroup, Integer> c_y;
    @FXML
    private TableColumn<StudyGroup, String> p_name;
    @FXML
    private TableColumn<StudyGroup, Integer> p_height;
    @FXML
    private TableColumn<StudyGroup, Integer> p_weight;
    @FXML
    private TableColumn<StudyGroup, String> p_color;
    @FXML
    private TableColumn<StudyGroup, Integer> l_x;
    @FXML
    private TableColumn<StudyGroup, Integer> l_y;
    @FXML
    private TableColumn<StudyGroup, Integer> l_z;
    @FXML
    private TableColumn<StudyGroup, String> l_name;
    @FXML
    private TableColumn<StudyGroup, String> creationDate;
    @FXML
    private TableColumn<StudyGroup, String> owner;


    public void collectionInit(List<StudyGroup> data){
        ObservableList<StudyGroup> list = FXCollections.observableList(data);
        table.setItems(list);
    }

}
