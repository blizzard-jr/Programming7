package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import org.example.UserInterface;
import org.example.island.commands.Filter;
import org.example.island.commands.Sort;

public class SortAndFilter {
    @FXML
    private Button sort;
    @FXML
    private Button filter;
    @FXML
    private ToggleButton upOrDown;
    @FXML
    private TextField filterValue;
    private String columnName;
    private MainScene mainScene;
    AnchorPane anchorPane;
    public void init(MainScene scene, AnchorPane anchorPane, String columnName){
        mainScene = scene;
        this.anchorPane = anchorPane;
        this.columnName = columnName;
    }
    public void upOrDown(){
        if(upOrDown.isSelected()){
            upOrDown.setText("Down");
        }else{
            upOrDown.setText("Up");
        }
    }

    public void sort(){
        Sort sorts = new Sort();
        sorts.setArguments(upOrDown.isSelected());
        sorts.setArguments(columnName);
        mainScene.process(sorts);
    }
    public void filter(){
        Filter filters = new Filter();
        filters.setArguments(filterValue.getText());
        filters.setArguments(columnName);
        mainScene.process(filters);
    }
}
